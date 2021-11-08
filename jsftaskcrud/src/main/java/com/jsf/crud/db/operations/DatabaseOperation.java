package com.jsf.crud.db.operations;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.faces.context.FacesContext;
 
import com.jsf.crud.TarefaBean;

public class DatabaseOperation {
 
    public static Statement stmtObj;
    public static Connection connObj;
    public static ResultSet resultSetObj;
    public static PreparedStatement pstmt;
 
    public static Connection getConnection(){  
        try{ 
        	Class.forName("org.postgresql.Driver");

        	String url = "jdbc:postgresql://localhost:5432/postgres";
        	Properties props = new Properties();
        	props.setProperty("user","postgres");
        	props.setProperty("password","123456");
        	
        	connObj = DriverManager.getConnection(url, props);

        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }  
        return connObj;
    }
 
    public static ArrayList<TarefaBean> getTarefaListFromDB() {
        ArrayList<TarefaBean> tarefaList = new ArrayList<TarefaBean>();  
        try {
            stmtObj = getConnection().createStatement();    
            resultSetObj = stmtObj.executeQuery("select * from tarefa_lista");    
            while(resultSetObj.next()) {  
                TarefaBean stuObj = new TarefaBean(); 
                stuObj.setId(resultSetObj.getInt("tarefa_id"));  
                stuObj.setTitulo(resultSetObj.getString("tarefa_titulo"));  
                stuObj.setStatus(resultSetObj.getString("tarefa_status"));  
                stuObj.setDescricao(resultSetObj.getString("tarefa_descricao"));  
                stuObj.setData(resultSetObj.getString("tarefa_data"));
                stuObj.setTelefone(resultSetObj.getString("tarefa_telefone"));
                tarefaList.add(stuObj);  
            }   
            System.out.println("Total Lista: " + tarefaList.size());
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        } 
        return tarefaList;
    }
 
    public static String saveTarefaDetailsInDB(TarefaBean newTarefaObj) {
        int saveResult = 0;
        String navigationResult = "";
        try {      
            pstmt = getConnection().prepareStatement("insert into tarefa_lista (tarefa_titulo, tarefa_status, tarefa_descricao, tarefa_data, tarefa_telefone) values (?, ?, ?, ?, ?)");         
            pstmt.setString(1, newTarefaObj.getTitulo());
            pstmt.setString(2, newTarefaObj.getStatus());
            pstmt.setString(3, newTarefaObj.getDescricao());
            pstmt.setString(4, newTarefaObj.getData());
            pstmt.setString(5, newTarefaObj.getTelefone());
            saveResult = pstmt.executeUpdate();
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        if(saveResult !=0) {
            navigationResult = "tarefaList.xhtml?faces-redirect=true";
        } else {
            navigationResult = "createTarefa.xhtml?faces-redirect=true";
        }
        return navigationResult;
    }
 
    public static String editTarefaListInDB(int tarefaId) {
        TarefaBean editTarefa = null;
        System.out.println("editTarefaListInDB() : Tarefa Id: " + tarefaId);
 
        Map<String,Object> sessionMapObj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
 
        try {
            stmtObj = getConnection().createStatement();    
            resultSetObj = stmtObj.executeQuery("select * from tarefa_lista where tarefa_id = "+tarefaId);    
            if(resultSetObj != null) {
                resultSetObj.next();
                editTarefa = new TarefaBean(); 
                editTarefa.setId(resultSetObj.getInt("tarefa_id"));
                editTarefa.setTitulo(resultSetObj.getString("tarefa_titulo"));
                editTarefa.setStatus(resultSetObj.getString("tarefa_status"));
                editTarefa.setDescricao(resultSetObj.getString("tarefa_descricao"));
                editTarefa.setData(resultSetObj.getString("tarefa_data"));
                editTarefa.setTelefone(resultSetObj.getString("tarefa_telefone"));
            }
            sessionMapObj.put("editTarefaObj", editTarefa);
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return "/editTarefa.xhtml?faces-redirect=true";
    }
 
    public static String updateTarefaDetailsInDB(TarefaBean updateTarefaObj) {
        try {
            pstmt = getConnection().prepareStatement("update tarefa_lista set tarefa_titulo=?, tarefa_status=?, tarefa_descricao=?, tarefa_data=?, tarefa_telefone=?"+ "where tarefa_id=?");    
            pstmt.setString(1,updateTarefaObj.getTitulo());  
            pstmt.setString(2,updateTarefaObj.getStatus());  
            pstmt.setString(3,updateTarefaObj.getDescricao());  
            pstmt.setString(4,updateTarefaObj.getData());
            pstmt.setString(5,updateTarefaObj.getTelefone()); 
            pstmt.setInt(6,updateTarefaObj.getId());  
            pstmt.executeUpdate();
            connObj.close();            
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return "/tarefaList.xhtml?faces-redirect=true";
    }
 
    public static String deleteTarefaListInDB(int tarefaId){
        System.out.println("deleteTarefaListInDB() : Tarefa Id: " + tarefaId);
        try {
            pstmt = getConnection().prepareStatement("delete from tarefa_lista where tarefa_id = "+ tarefaId);  
            pstmt.executeUpdate();  
            connObj.close();
        } catch(Exception sqlException){
            sqlException.printStackTrace();
        }
        return "/tarefaList.xhtml?faces-redirect=true";
    }
}