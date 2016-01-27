package com.videoapp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import com.microsoft.windowsazure.services.core.ServiceException;
import com.microsoft.windowsazure.services.media.models.AssetInfo;
import com.videoapp.utilities.AzureMediaServices;
import com.videoapp.utilities.VideoDAO;


public class VideoController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<VideoDAO> videos=null;
		try {
			videos=VideoDAO.getAllVideos();
		} catch (SQLException e) {
	
			e.printStackTrace();
		}
		req.setAttribute("videos", videos);
		getServletContext().getRequestDispatcher("/overview.jsp").forward (req, resp);
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("post");
		response.setContentType("text/html");
		//PrintWriter out = response.getWriter();

		

		boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
		if (!isMultipartContent) {
			System.out.println("You are not trying to upload<br/>");
			return;
		}
		System.out.println("You are trying to upload<br/>");

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> fields = upload.parseRequest(new ServletRequestContext(request));
			Iterator<FileItem> it = fields.iterator();
			if (!it.hasNext()) {
				System.out.println("No fields found");
				return;
			}
			System.out.println("<table border=\"1\">");
			String tag="";
			String uri="";
			while (it.hasNext()) {
				System.out.println("<tr>");
				FileItem fileItem = it.next();
				boolean isFormField = fileItem.isFormField();
				if (isFormField) {
					System.out.println("regular form field FIELD NAME: " + fileItem.getFieldName() + 
							"\n FIFELD VALUE:" + fileItem.getString()
							);
					tag=fileItem.getString();
					
				} else {
					System.out.println(
							
							"NAME: " + fileItem.getName() +
							"CONTENT TYPE: " + fileItem.getContentType() +
							"IZE (BYTES): " + fileItem.getSize()
						
							);
					AzureMediaServices ams=new AzureMediaServices();
					InputStream videoStream=fileItem.getInputStream();
					AssetInfo inputAsset=ams.uploadFile(videoStream, fileItem.getName());
					AssetInfo preparedAsset=ams.encode2(inputAsset);
					uri=ams.getStreamingOriginLocator(preparedAsset);
					videoStream.close();
					
				}
			
			}
			VideoDAO.insert(tag, uri);
			
		
		} catch (FileUploadException | ServiceException | InterruptedException | SQLException e) {
			e.printStackTrace();
		}
	}
		
	
	
//	private String getFileName(final Part part) {
//	    final String partHeader = part.getHeader("content-disposition");
//	    LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
//	    for (String content : part.getHeader("content-disposition").split(";")) {
//	        if (content.trim().startsWith("filename")) {
//	            return content.substring(
//	                    content.indexOf('=') + 1).trim().replace("\"", "");
//	        }
//	    }
//	    return null;
//	}
	
}
