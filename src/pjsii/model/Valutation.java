package pjsii.model;

public class Valutation {
	
	private double precisionAuthor;
	private double recallAuthor;
	private double precision;
	private double recall;
	private double fMeasureAuthor;
	private double fMeasure;
	private boolean isValid;
	
	public Valutation(){
		this.isValid = false;
	}
	
	public Valutation(double precisionAuthor, double recallAuthor, double precision, double recall){
		this.precisionAuthor = precisionAuthor;
		this.recallAuthor = recallAuthor;
		this.precision = precision;
		this.recall = recall;
		this.fMeasureAuthor = 2*((this.precisionAuthor * this.recallAuthor)/(this.precisionAuthor + this.recallAuthor));
		this.fMeasure = 2*((this.precision * this.recall)/(this.precision + this.recall));
		this.isValid = true;
	}

	public double getPrecisionAuthor() {
		return precisionAuthor;
	}

	public void setPrecisionAuthor(double precisionAuthor) {
		this.precisionAuthor = precisionAuthor;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getRecallAuthor() {
		return recallAuthor;
	}

	public void setRecallAuthor(double recallAuthor) {
		this.recallAuthor = recallAuthor;
	}

	public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getfMeasureAuthor() {
		return fMeasureAuthor;
	}

	public void setfMeasureAuthor(double fMeasureAuthor) {
		this.fMeasureAuthor = fMeasureAuthor;
	}
	
	public double getfMeasure() {
		return fMeasure;
	}

	public void setfMeasure(double fMeasure) {
		this.fMeasure = fMeasure;
	}
	
	public boolean isNull(){
		return !isValid;
	}

	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}
}