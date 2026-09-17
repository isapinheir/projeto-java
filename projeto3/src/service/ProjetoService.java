package service;

import java.util.ArrayList;
import java.util.List;

import model.Projeto;

public class ProjetoService{
	private List <Projeto> projetos;

	public ProjetoService(){
		projetos = new ArrayList<>();
	}

	public void adicionar(Projeto projeto){
		projetos.add(projeto);	
	}
    public List<Projeto> listar(){
        return projetos;    
    }
}
