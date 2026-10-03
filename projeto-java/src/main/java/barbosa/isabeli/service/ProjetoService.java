package barbosa.isabeli.service;

import java.util.ArrayList;
import java.util.List;

import barbosa.isabeli.dao.ProjetoCSV;
import barbosa.isabeli.model.Projeto;

public class ProjetoService{
	private List <Projeto> projetos;
	private ProjetoCSV dao;

	public ProjetoService(){
		projetos = new ArrayList<>();
		dao = new ProjetoCSV();
	}

	public void carregar() throws Exception{
		projetos = dao.listar();
	}

	public void salvar() throws Exception{
		dao.salvar(projetos);
	}

	// -- ADICIONAR --

	public boolean adicionar(Projeto projeto) throws Exception{
		if(projeto.getNome() == null ||
		projeto.getNome().isBlank()){
			return false;
		}
		if(buscarPorId(projeto.getId()) != null){
			return false;
		}

		projetos.add(projeto);
		return true;
	}

	// -- ALTERAR --

	public boolean alterarProjeto(Projeto projetoAtualizado) throws Exception{
		Projeto projeto = buscarPorId(projetoAtualizado.getId());
		if(projeto == null){
			return false;
		}
		projeto.setNome(projetoAtualizado.getNome());
		projeto.setDescricao(projetoAtualizado.getDescricao());
		projeto.setCategoria(projetoAtualizado.getCategoria());
		projeto.setStatus(projetoAtualizado.getStatus());
		return true;
	}

	public boolean alterarNome(int id, String novoNome)throws Exception{
		Projeto projeto = buscarPorId(id);
		if(projeto == null){
			return false;
		}
		projeto.setNome(novoNome);
		return true;
	}
	public boolean alterarDescricao(int id, String novaDescricao) throws Exception{
		Projeto projeto = buscarPorId(id);
		if(projeto == null){
			return false;
		}
		projeto.setDescricao(novaDescricao);
		return true;
	}
	public boolean alterarCategoria(int id, String novaCategoria) throws Exception{
		Projeto projeto = buscarPorId(id);
		if(projeto == null){
			return false;
		}
		projeto.setCategoria(novaCategoria);
		return true;
	}
	public boolean alterarStatus(int id, String novoStatus) throws Exception{
		Projeto projeto = buscarPorId(id);
		if(projeto == null){
			return false;
		}
		projeto.setStatus(novoStatus);
		return true;
	}

	// -- BUSCAR E LISTAR --

	public List<Projeto> listar() throws Exception {
	    if (this.projetos == null || this.projetos.isEmpty()) {
	        this.projetos = dao.listar();
	    }
	    return this.projetos;
	}

	public Projeto buscarPorId(int id) throws Exception{
        for(Projeto projeto : projetos){
        	if (this.projetos == null || this.projetos.isEmpty()) {
    	        this.projetos = dao.listar();
    	    }
			if(projeto.getId() == id){
				return projeto;
			}
        }
		return null;
    }

	public List<Projeto> buscarPorNome(String texto){
		List<Projeto> resultado = new ArrayList<>();
		for(Projeto projeto : projetos){
			if(projeto.getNome().toLowerCase().contains(texto.toLowerCase())){
				resultado.add(projeto);
			}
		}
		return resultado;
	}

	public List<Projeto> buscarPorDescricao(String texto){
		List<Projeto> resultado = new ArrayList<>();
		for(Projeto projeto : projetos){
			if(projeto.getDescricao().toLowerCase().contains(texto.toLowerCase())){
				resultado.add(projeto);
			}
		}
		return resultado;
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

	// -- REMOVER --

	public boolean removerPorId(int id) throws Exception{
    	Projeto projeto = buscarPorId(id);
		if(projeto != null){
			projetos.remove(projeto);
			return true;
		}
		return false;
    }
}