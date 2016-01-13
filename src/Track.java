import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@NamedQuery(name = "findAllTrack", query = "SELECT t FROM Track t")
@Table(name="tbl_track")
public class Track {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(nullable = false)
	private String idSpotify;
	
	@Column
	private String name;
	
	@Column
	private String author;
	
	@Column
	private String album;
	
	@Column
	private String url;
	
	@Column
	private double popularity;
	
	@OneToMany(mappedBy="track", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Tweet> tweets;
	
	public Track(){
		this.tweets = new ArrayList<Tweet>();
	}
	
	public Track(String idSpotify, String name, String author, String album, double popularity){
		this.idSpotify = idSpotify;
		this.name = name;
		this.author = author;
		this.album = album;
		this.url = "https://play.spotify.com/track/" + idSpotify;
		this.popularity = popularity;
		this.tweets = new ArrayList<Tweet>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIdSpotify() {
		return idSpotify;
	}

	public void setIdSpotify(String idSpotify) {
		this.idSpotify = idSpotify;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getPopularity() {
		return popularity;
	}

	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	public void addTweet(Tweet tweet){
		this.tweets.add(tweet);
	}
}
