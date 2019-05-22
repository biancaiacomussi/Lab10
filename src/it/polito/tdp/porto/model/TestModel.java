package it.polito.tdp.porto.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		System.out.println("TODO: write a Model class and test it!");
		
		model.setGrafo();
		System.out.println(model.getGrafo());
		System.out.println("Archi: "+model.getGrafo().edgeSet().size());
		System.out.println("Verici: "+model.getGrafo().vertexSet().size());
		Author a = new Author (2154, "Corno","Fulvio");
		System.out.println(model.getCoautori(a));
		System.out.println(model.nonVicini(a));
	}

}
