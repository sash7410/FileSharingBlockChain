/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Database.DBQuery;
import Logic.ImageResizer;
import Logic.info;
import com.oreilly.servlet.MultipartRequest;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sumit
 */
public class upload extends HttpServlet {
 String fname="",user="";
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
             
    user = request.getParameter("user");
    fname = request.getParameter("fname");
  
            System.out.println("user="+user);
            System.out.println("fname="+fname);
         
   String paramname=null,fn="";

   String fileDir = info.path;
//    String fsdir=info.path+"/web/"+user+"/"+fname+"/";
//        System.out.println(">>"+fileDir);
//        File f =new File(fsdir);
//         if(!f.exists())
//         {
//         f.mkdirs();
//         }
//  
  
        MultipartRequest multi = new MultipartRequest(request, fileDir,	10 * 1024 * 1024); // 10MB
                       
        String fPath="";
        Enumeration files = multi.getFileNames();	
	while (files.hasMoreElements()) 
	{
            paramname = (String) files.nextElement();
            
            if(paramname != null && paramname.equals("uploaded_file"))
            {
		fn = multi.getFilesystemName(paramname);
                fPath = fileDir+fn;
                System.out.println("::::::::::::="+fn);
                System.out.println("::::::::::::="+fPath);
                
                //String[] name = filePath.split(".");
            }
        }

out.println("ok");
  //Use SHA-256 algorithm
        MessageDigest shaDigest;
        String hash="";
try {
            //Create checksum for this file
            File fileh = new File(fPath);
            shaDigest = MessageDigest.getInstance("SHA-256");
            //SHA-1 checksum 
            hash = Logic.getFileChecksum.getFileChecksum(shaDigest, fileh);
            System.out.println(">>>"+hash);
            
            File fh=new File(info.py_path+user+"_"+fname+"_hash.txt");//creates hashtxt file with userid and fname
            FileWriter fwh=new FileWriter(fh);
            fwh.write(hash);
            fwh.close();
            
        } catch (Exception ex) {
           ex.printStackTrace();
        }     



        File fs=new File(fPath);
        File fd=new File(info.py_path+""+user+"_"+fname);
        copyFileUsingStream(fs,fd);
        
        File fh=new File(info.py_path+"fname.txt");
        FileWriter fwh=new FileWriter(fh);
        fwh.write(user+"_"+fname);
        fwh.close();
        
        
        File fh1=new File(info.py_path+"status.txt");
        FileWriter fwh1=new FileWriter(fh1);
        fwh1.write("upload");
        fwh1.close();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(upload.class.getName()).log(Level.SEVERE, null, ex);
            }
            fs.delete();
        
        }
    }
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

}
