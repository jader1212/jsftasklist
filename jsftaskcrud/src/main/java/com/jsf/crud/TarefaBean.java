package com.jsf.crud;
 
import java.util.ArrayList;
 
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

 
import com.jsf.crud.db.operations.DatabaseOperation;

@ManagedBean @RequestScoped
public class TarefaBean {
 
    private int id;  
    private String titulo;  
    private String status;  
    private String descricao;  
    private String data;
 
    public ArrayList<TarefaBean>tarefaListFromDB;
    
    public int getId() {
        return id;  
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getTitulo() {
        return titulo;
    }
 
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
 
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }
 
    public String getDescricao() {
        return descricao;
    }
 
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
 
    public String getData() {
        return data;
    }
 
    public void setData(String data) {
        this.data = data;
    }
     
    @PostConstruct
    public void init() {
        tarefaListFromDB = DatabaseOperation.getTarefaListFromDB();
    }
 
    public ArrayList<TarefaBean> tarefaList() {
		return tarefaListFromDB;
	}
     
    public String saveTarefaDetails(TarefaBean newTarefaObj) {
        return DatabaseOperation.saveTarefaDetailsInDB(newTarefaObj);
    }
     
    public String editTarefaList(int tarefaId) {
        return DatabaseOperation.editTarefaListInDB(tarefaId);
    }
     
    public String updateTarefaDetails(TarefaBean updateTarefaObj) {
        return DatabaseOperation.updateTarefaDetailsInDB(updateTarefaObj);
    }
     
    public String deleteTarefaList(int tarefaId) {
        return DatabaseOperation.deleteTarefaListInDB(tarefaId);
    }
}  