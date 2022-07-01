
package com.study.attach.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.attach.service.IAttachService;
import com.study.attach.vo.AttachVO;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.free.web.FreeBoardController;

@Controller
public class AttachController {
	@Value("#{util['file.upload.path']}")
	private String uploadPath;
	@Autowired
	private IAttachService attachService;
	
	
	//img파일 썸네일 
	@GetMapping("/attach/showImg.wow")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName,String filePath){	
		File file=new File(uploadPath+File.separatorChar +filePath,fileName);
		ResponseEntity<byte[]> result=null;
		try {
			HttpHeaders headers=new HttpHeaders();
			headers.add("Content-Type", Files.probeContentType(file.toPath()));
			result=new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),headers,HttpStatus.OK );
		}catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	

	// @PathVariable 사용하여 url상의 경로를 변수에 할당 "/attach/download/25625"
	@RequestMapping("/attach/download/{atchNo:[0-9]{1,16}}")
	public void process(@PathVariable(name = "atchNo") int atchNo, HttpServletResponse resp) throws Exception {
		try {
			// 서비스를 통해 첨부파일 가져오기
			AttachVO vo = attachService.getAttach(atchNo);
			// 파일명에 한글이 있는경우 처리
			String originalName = new String(vo.getAtchOriginalName().getBytes("utf-8"), "iso-8859-1");
			String filePath = uploadPath + File.separatorChar + vo.getAtchPath();
			String fileName = vo.getAtchFileName();

			// 경로에 있는 파일 찾기
			File f = new File(filePath, fileName);
			if (!f.isFile()) {
				throw new BizNotFoundException("해당 첨부파일이 존재하지 않습니다.");
			}
			// 다운로드를 위한 헤더 생성
			resp.setHeader("Content-Type", "application/octet-stream;");
			resp.setHeader("Content-Disposition", "attachment;filename=\"" + originalName + "\";");
			resp.setHeader("Content-Transfer-Encoding", "binary;");
			resp.setContentLength((int) f.length()); // 진행율
			resp.setHeader("Pragma", "no-cache;");
			resp.setHeader("Expires", "-1;");
			// 저장된 파일을 응답객체의 스트림으로 내보내기, resp의 outputStream에 해당파일을 복사
			FileUtils.copyFile(f, resp.getOutputStream());
			resp.getOutputStream().close();
			
//			FileInputStream fileInputStream = new FileInputStream(path); // 파일 읽어오기 
//        	OutputStream out = response.getOutputStream();
//        	
//        	int read = 0;
//                byte[] buffer = new byte[1024];
//                while ((read = fileInputStream.read(buffer)) != -1) { // 1024바이트씩 계속 읽으면서 outputStream에 저장, -1이 나오면 더이상 읽을 파일이 없음
//                    out.write(buffer, 0, read);
//                }
			
			attachService.increaseDownHit(atchNo);
		} catch (BizNotFoundException e) {
			printMessage(resp, "해당 첨부파일이 존재하지 않습니다.");
		} catch (BizNotEffectedException e) {
			e.printStackTrace(); // 거의 일어나지않기때문에...
		} catch (IOException e) {
			resp.reset();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
		}
	}

	// 정상적인 다운로드가 안될 경우 메시지 처리
	private void printMessage(HttpServletResponse resp, String msg) throws Exception {
		resp.setCharacterEncoding("utf-8");
		resp.setHeader("Content-Type", "text/html; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		// target이 지정되지 않은 경우 history.back() 으로 처리
		out.println("<script type='text/javascript'>");
		out.println(" alert('" + msg + "');");
		out.println(" self.close();");
		out.println("</script>");
		out.println("<h4>첨부파일 문제 " + msg + "</h4>");
		out.println("<button onclick='self.close()'>닫기</button>");
	}
}