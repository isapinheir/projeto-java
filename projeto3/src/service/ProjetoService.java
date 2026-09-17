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
	public Projeto buscarPorId(int id){
        for(Projeto projeto : projetos){
			if(projeto.getId() == id){
				return projeto;
			}
        }
		return null;
    }
	public List<Projeto> buscarPorCategoria(String categoria){
		List <Projeto> resultado = new ArrayList<>();
		for(Projeto projeto : projetos){
			if(projeto.getCategoria().equalsIgnoreCase(categoria)){
				resultado.add(projeto);
			}
		}
		return resultado;
	}
	public List<Projeto> buscarPorStatus(String status){
		List <Projeto> resultado = new ArrayList<>();
		for(Projeto projeto : projetos){
			if(projeto.getStatus().equalsIgnoreCase(status)){
				resultado.add(projeto);
			}	
		}
		return(resultado);
	}
}
