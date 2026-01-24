package gr.unipi.ddim.meallabapi.models;

public enum ImageSize {
	SMALL("/small"), MEDIUM("/medium"), LARGE("/large"), FULL("");

	private final String urlSuffix;

	ImageSize(String urlSuffix) {
		this.urlSuffix = urlSuffix;
	}

	public String getUrlSuffix() {
		return urlSuffix;
	}
}