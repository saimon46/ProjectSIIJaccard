import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import pjsii.jaccard.JaccardSimilarity;

public class Jaccard {
	public static void main(String[] args) throws IOException {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("projectSII");
		EntityManager em = emf.createEntityManager();
		JaccardSimilarity jaccard = new JaccardSimilarity();
		
		String userId = "13229";
		
		jaccard.runOneUserPercent(em, userId, 70);
		
		
		//jaccard.runOneUser(em, userId);
		
		/*
		
		javax.persistence.Query qUser = em.createQuery("SELECT u FROM User u WHERE u.id = :id");
    	qUser.setParameter("id", userId);
		List<User> listUser = new ArrayList<User>(qUser.getResultList());
		User user = listUser.get(0);
		
		javax.persistence.Query qUsers = em.createQuery("SELECT u FROM User u GROUP BY u HAVING COUNT(u.tweets) > 1");
		List<User> users = new ArrayList<User>(qUsers.getResultList());
		
		//System.out.println("Dimensione users "+users.size());
		
		List<Tweet>	tweets = new ArrayList<Tweet>();
		for(User current:users){
			tweets.addAll(current.getTweets());
		}
		
		//System.out.println("Dimensione tweet "+tweets.size());
		
		List<Tweet>	tweets80Training = new ArrayList<Tweet>();
		List<Tweet>	tweets20Test = new ArrayList<Tweet>();
		
		int size80 = tweets.size()/100*80;
		int cont = 0;
		
		for(Tweet tweet:tweets){
			if(cont < size80)
				tweets80Training.add(tweet);
			else
				tweets20Test.add(tweet);
			cont++;
		}
		
		
		List<Track> tracksUser = user.getTracksInTweets(tweets80Training);
		int a = 0;
		a=1;
		
		*/
	}
}


