/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sachin.seobox.archives;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sachin
 */
public class History extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    PrintWriter out;
    String[] files = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        out = response.getWriter();
        String address = request.getRequestURL().toString().substring(0,
                request.getRequestURL().toString().lastIndexOf('/'));
        String dir = request.getParameter("dir");
        dir = java.net.URLDecoder.decode(dir, "utf-8");
        String realPath = getServletContext().getRealPath("Reports");
        if ("/".equals(dir)) {
            dir = getServletContext().getRealPath("Reports");
        } else {
            dir = getServletContext().getRealPath(dir);
        }

        dir += File.separator;
        if (new File(dir).exists()) {
            files = new File(dir).list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return !name.equalsIgnoreCase("assets");
                }
            });
            List<String> list = Arrays.asList(files);
            Collections.reverse(list);
            out.print("<ul class=\"jqueryFileTree\" style=\"display: none;\">");
            // All dirs
            for (String file : files) {
                if (new File(dir, file).isDirectory()) {
                    if (!file.equalsIgnoreCase("assets")) {
                        String rel = dir.substring(realPath.length() - 7, dir.length()) + file;
                        out.print("<li class=\"directory collapsed\"><a href=\"#\" rel=\"" + rel + "\">" + file
                                + "</a></li>");
                    }
                } else {
                    if (file.equalsIgnoreCase("Report.html")) {
                        String path = dir.substring(dir.indexOf("Reports"));
                        path = path.replaceAll("//", "/");
                        path = address + "/" + path + file;
                        out.print("<li><i class=\"fa fa-globe\"></i><a target='_blank' href='" + path + "' rel='" + path + "'>View Report</a></li>");
                    }
                }
            }            
            out.print("</ul>");
        } else {
            out.print("No History");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on
    // the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
