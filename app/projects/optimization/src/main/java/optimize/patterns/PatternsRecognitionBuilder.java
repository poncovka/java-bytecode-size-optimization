package jbyco.optimize.patterns;

public interface PatternsRecognitionBuilder<Symbol, Item> {
	
	public void addPattern(Pattern<Item> pattern);
	public PatternsRecognitionBuilder<Symbol, Item> build();
	
}
