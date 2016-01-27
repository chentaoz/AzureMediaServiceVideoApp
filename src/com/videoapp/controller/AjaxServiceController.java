package com.videoapp.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.videoapp.utilities.VideoDAO;

/**
 * Servlet implementation class AjaxServiceController
 */
@WebServlet("/AjaxServiceController")
public class AjaxServiceController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxServiceController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String service=request.getParameter("sev");
		switch (service){
			
			case "tagSev":try {
							tagFunc(request.getParameter("tag"),request.getParameter("id"));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}break;
			case "pageSev":pageFunc();break;
			default:break;
		}
		
		
	}
	
	private void tagFunc(String tag,String id) throws SQLException{
		System.out.println("id:"+id+" tag:"+tag);
		VideoDAO.updateTag(id, tag);
	}
	private void pageFunc(){
		
	}
	

}
