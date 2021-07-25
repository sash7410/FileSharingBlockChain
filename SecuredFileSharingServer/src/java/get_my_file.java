/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Logic.info;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sumit
 */
public class get_my_file extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
           String user=request.getParameter("user");
           String fname=request.getParameter("fname");
            System.out.println(user+"==="+fname);
            File fh1=new File(info.py_path+"fname.txt");
            FileWriter fwh1=new FileWriter(fh1);
            fwh1.write(fname);
            fwh1.close();
            File fh=new File(info.py_path+"status.txt");
            FileWriter fwh=new FileWriter(fh);
            fwh.write("get_file");
            fwh.close();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(get_my_file.class.getName()).log(Level.SEVERE, null, ex);
            }
            String fhash="";
            File file1 = new File(info.py_path+fname+"_hash.txt"); 

            BufferedReader br = new BufferedReader(new FileReader(file1)); 

            String st; 
            while ((st = br.readLine()) != null) {
            System.out.println(st); 
            fhash=st;
            
            }
            System.out.println("fhash="+fhash);
            MessageDigest shaDigest;
            String hash="";
            try {
            //Create checksum for this file
            File fileh = new File(info.py_path+fname);
            shaDigest = MessageDigest.getInstance("SHA-256");
            //SHA-1 checksum 
            hash = Logic.getFileChecksum.getFileChecksum(shaDigest, fileh);
            System.out.println(">>>"+hash);
            
           
            
            } catch (Exception ex) {
               ex.printStackTrace();
            }     
            if(hash.equals(fhash))
            {
                System.out.println("File verified");
                File fs=new File(info.py_path+fname);
                File fd=new File(info.path+fname);
                copyFileUsingStream(fs,fd);
                out.print("ok");
            }
            else{
                System.out.println("File not verified");
                out.print("notok");
            
            }
            

            
            
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
private static void copyFileUsingStream(File source, File dest) throws IOException {
    InputStream is = null;
    OutputStream os = null;
    try {
        is = new FileInputStream(source);
        os = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    } finally {
        is.close();
        os.close();
    }
}
}
