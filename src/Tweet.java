import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tbl_tweet")
public class Tweet {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column
	private int count;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.PERSIST)
	private User user;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Track track;
	
	public Tweet(){
		this.count = 1;
	}
	
	public Tweet(User user, Track track){
		this.count = 1;
		this.user = user;
		this.track = track;
		this.track.addTweet(this);
	}
	
	public int incrementCount(){
		this.count++;
		return this.count;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
		this.track.addTweet(this);
	}
}
