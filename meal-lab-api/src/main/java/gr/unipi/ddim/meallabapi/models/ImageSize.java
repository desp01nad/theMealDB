package gr.unipi.ddim.meallabapi.models;

/** Image size variants supported by TheMealDB image URLs. */
public enum ImageSize {
	SMALL("/small"), MEDIUM("/medium"), LARGE("/large"), FULL("");

	private final String urlSuffix;

	ImageSize(String urlSuffix) {
		this.urlSuffix = urlSuffix;
	}

	/** Returns the URL suffix used to request this size. */
	public String getUrlSuffix() {
		return urlSuffix;
	}
}
