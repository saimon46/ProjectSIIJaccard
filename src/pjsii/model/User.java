package pjsii.model;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@NamedQuery(name = "findAllUsers", query = "SELECT u FROM User u")
@Table(name="tbl_user")
public class User {	
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@Column(unique=true)
	private long idTwitter;
	
	@Column(nullable = false, unique=true)
	private String name;
	
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private List<Tweet> tweets;
	
	public User(){
		this.tweets = new ArrayList<Tweet>();
	}
	
	public User(long idTwitter, String name){
		this.idTwitter = idTwitter;
		this.name = name;
		this.tweets = new ArrayList<Tweet>();
	}
	
	public void addTweet(Tweet tweet){
		this.tweets.add(tweet);
		tweet.setUser(this);
	}
	
	public Tweet hasTrackInTweet(String idSpotify){
		Tweet tweet = null;
		
		for(Tweet tweetIterator:tweets)
			if(tweetIterator.getTrack().getIdSpotify().equals(idSpotify))
				tweet = tweetIterator;
					
		return tweet;
	}
	
	public List<Track> getTracks(){
		List<Track> tracks = new ArrayList<Track>();
		Track track = null;
		
		for(Tweet tweet:this.tweets){
			track = tweet.getTrack();
			if(!tracks.contains(track))
				tracks.add(track);
		}
		
		return tracks;
	}
	
	public List<Track> getTracksInTweets(List<Tweet> tweets){
		List<Track> tracks = new ArrayList<Track>();
		Track track = null;
		
		for(Tweet tweet:tweets){
			track = tweet.getTrack();
			if(!tracks.contains(track) && this.tweets.contains(tweet))
				tracks.add(track);
		}
		
		return tracks;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public long getIdTwitter() {
		return idTwitter;
	}

	public void setIdTwitter(long idTwitter) {
		this.idTwitter = idTwitter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
}
