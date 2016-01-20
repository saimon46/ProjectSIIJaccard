package pjsii.jaccard;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import pjsii.model.*;

public class JaccardSimilarity {
	final static double cAuthor = 0.5;
	final static double cTrack = 0.5;
	
    public void runAllForSize(EntityManager em, int size) throws IOException {
    	javax.persistence.Query qUsers = em.createQuery("SELECT tw.user AS uid FROM Tweet tw GROUP BY tw.user ORDER BY COUNT(tw.id) DESC");
		List<User> users = new ArrayList<User>(qUsers.getResultList());
		
		ArrayList<ArrayList<Double>> similarity = getSimilarityMatrix(em, users, size, cAuthor, cTrack);
		
		FileWriter fOutstream1 = new FileWriter("Log.txt");
        BufferedWriter out1 = new BufferedWriter(fOutstream1);
		
		for(int i = 0; i<size; i++){
			String s = "";
			System.out.print("Utente n° "+(i+1)+": ");
			
			if(i%20 == 0){
				out1.write("Analizzati " + (i+1) + " utenti su " + size + "...\n");
			}
			
			for(int j = 0; j<size; j++){
				s+=similarity.get(i).get(j)+" - ";
			}
			System.out.println(s);
		}
		
		System.out.println("-----------Analisi similarità completata-----------");
		out1.write("-----------Analisi similarità completata-----------\n");
		
		List<Double> max = new ArrayList<Double>();
		List<User> similarUser = new ArrayList<User>();
		
		for(int i = 0; i<size; i++){
			max.add(0.0);
			for(int j = 0; j<size; j++)
				if(max.get(i) < similarity.get(i).get(j) && similarity.get(i).get(j) != 1)
					max.set(i, similarity.get(i).get(j));
		}
		
		for(int i = 0; i<size; i++){
			if(max.get(i) != 0)
				for(int j = 0; j<size; j++){
					if(max.get(i) == similarity.get(i).get(j)){
						similarUser.add(users.get(j));
						break;
					}
				}
			else
				similarUser.add(null);
		}
		
		System.out.println("-----------Calcolo utenti simili completato-----------");
		out1.write("-----------Calcolo utenti simili completato-----------\n");
		
		FileWriter fOutstream2 = new FileWriter("Similarity.txt");
        BufferedWriter out2 = new BufferedWriter(fOutstream2);
		
		for(int i = 0; i<size; i++){
			if(similarUser.get(i) != null){
				System.out.println(users.get(i).getName() + " - " + similarUser.get(i).getName() + " - " + max.get(i));
				out2.write(users.get(i).getName() + " - " + similarUser.get(i).getName() + " - " + max.get(i));
			}else{
				System.out.println(users.get(i).getName() + " - NON SIMILE A NESSUN ALTRO UTENTE");
				out2.write(users.get(i).getName() + " - NON SIMILE A NESSUN ALTRO UTENTE");
			}
		}
		
		out1.close();
		out2.close();

    }
    
    public void runOneUser(EntityManager em, String userId) throws IOException {
    	javax.persistence.Query qUser = em.createQuery("SELECT u FROM User u WHERE u.id = :id");
    	qUser.setParameter("id", userId);
		List<User> listUser = new ArrayList<User>(qUser.getResultList());
		User user = listUser.get(0);
		
    	javax.persistence.Query qUsers = em.createQuery("SELECT u FROM User u, Tweet tw WHERE tw.user = u GROUP BY u HAVING COUNT(tw) > 1");
		List<User> users = new ArrayList<User>(qUsers.getResultList());
		
		ArrayList<Double> similarity = getSimilarityVector(em, user, users, cAuthor, cTrack);
		
		FileWriter fOutstream1 = new FileWriter("Log.txt");
        BufferedWriter out1 = new BufferedWriter(fOutstream1);
		
		System.out.println("-----------Analisi similarità completata-----------\n");
		out1.write("-----------Analisi similarità completata-----------\n");
		
		double max1 = 0.0;
		double max2 = 0.0;
		User similarUserFirst = null;
		User similarUserSecond = null;
		
		for(int i = 0; i<users.size(); i++)
			if(max1 < similarity.get(i) && similarity.get(i) != 1){
				max2 = max1;
				max1 = similarity.get(i);
				similarUserSecond = similarUserFirst;
				similarUserFirst = users.get(i);
			}else
				if(max2 < similarity.get(i) && similarity.get(i) != 1){
					max2 = similarity.get(i);
					similarUserSecond = users.get(i);
			}
		
		System.out.println("-----------Calcolo utente simile completato-----------\n");
		out1.write("-----------Calcolo utente simile completato-----------\n");
		
		FileWriter fOutstream2 = new FileWriter("Similarity.txt");
        BufferedWriter out2 = new BufferedWriter(fOutstream2);
		
		if(similarUserFirst!= null){
			System.out.println(user.getName() + " - " + similarUserFirst.getName() + " - " + max1);
			out2.write(user.getName() + " - " + similarUserFirst.getName() + " - " + max1);
			
			System.out.println(user.getName() + " - " + similarUserSecond.getName() + " - " + max2);
			out2.write(user.getName() + " - " + similarUserSecond.getName() + " - " + max2);
			
			List<Track> suggestedTracks = getListSuggestedTracks(user, similarUserFirst, similarUserSecond);
			
			String s2 = "\nTracce utente preso in considerazione: \n";
			List<Track> tracksUser = user.getTracks();
			for(Track track:tracksUser){
				s2 += track.getAuthor() + " - " + track.getName() + "\n";
			}
			
			s2 += "\nTracce primo utente simile: \n";
			List<Track> tracksFirst = similarUserFirst.getTracks();
			for(Track track:tracksFirst){
				s2 += track.getAuthor() + " - " + track.getName() + "\n";
			}
			
			s2 += "\nTracce secondo utente simile: \n";
			List<Track> tracksSecond = similarUserSecond.getTracks();
			for(Track track:tracksSecond){
				s2 += track.getAuthor() + " - " + track.getName() + "\n";
			}
			
			s2 += "\nTracce suggerite: \n";
			for(Track track:suggestedTracks){
				s2 += track.getAuthor() + " - " + track.getName() + "\n";
			}
			System.out.println(s2);
			out2.write(s2);
		}else{
			System.out.println(user.getName() + " - NON SIMILE A NESSUN ALTRO UTENTE");
			out2.write(user.getName() + " - NON SIMILE A NESSUN ALTRO UTENTE");
		}
		
		out1.close();
		out2.close();
    }
    
    public void runOneUser70percent(EntityManager em, String userId) throws IOException {
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
		
		Collections.shuffle(tweets);
		
		//System.out.println("Dimensione tweet "+tweets.size());
		
		List<Tweet>	tweets70Training = new ArrayList<Tweet>();
		List<Tweet>	tweets30Test = new ArrayList<Tweet>();
		
		int size70 = tweets.size()/100*70;
		int cont = 0;
		
		for(Tweet tweet:tweets){
			if(cont < size70)
				tweets70Training.add(tweet);
			else
				tweets30Test.add(tweet);
			cont++;
		}
		
		ArrayList<Double> similarity = getSimilarityVectorFromTweets(em, user, users, tweets70Training, cAuthor, cTrack);
		
		FileWriter fOutstream1 = new FileWriter("Log.txt");
        BufferedWriter out1 = new BufferedWriter(fOutstream1);
		
		System.out.println("-----------Analisi similarità completata-----------\n");
		out1.write("-----------Analisi similarità completata-----------\n");
		
		double max1 = 0.0;
		double max2 = 0.0;
		User similarUserFirst = null;
		User similarUserSecond = null;
		
		for(int i = 0; i<users.size(); i++)
			if(max1 < similarity.get(i) && similarity.get(i) != 1){
				max2 = max1;
				max1 = similarity.get(i);
				similarUserSecond = similarUserFirst;
				similarUserFirst = users.get(i);
			}else
				if(max2 < similarity.get(i) && similarity.get(i) != 1){
					max2 = similarity.get(i);
					similarUserSecond = users.get(i);
			}
		
		System.out.println("-----------Calcolo utente simile completato-----------\n");
		out1.write("-----------Calcolo utente simile completato-----------\n");
		
		FileWriter fOutstream2 = new FileWriter("Similarity.txt");
        BufferedWriter out2 = new BufferedWriter(fOutstream2);
		
		if(similarUserFirst!= null){
			System.out.println(user.getName() + " - " + similarUserFirst.getName() + " - " + max1);
			out2.write(user.getName() + " - " + similarUserFirst.getName() + " - " + max1);
			
			System.out.println(user.getName() + " - " + similarUserSecond.getName() + " - " + max2);
			out2.write(user.getName() + " - " + similarUserSecond.getName() + " - " + max2);
			
			List<Track> suggestedTracks = getListSuggestedTracksFromTweets(user, similarUserFirst, similarUserSecond, tweets70Training);
			List<Track> tracks20Test = user.getTracksInTweets(tweets30Test);
			
			String s2 = "\nTracce utente nel solo 80% del database: \n";
			List<Track> tracksUser = user.getTracksInTweets(tweets70Training);
			for(Track track:tracksUser){
				s2 += track.getAuthor() + " - " + track.getName() + "\n";
			}
			
			s2 += "\nTracce primo utente simile: \n";
			List<Track> tracksFirst = similarUserFirst.getTracksInTweets(tweets70Training);
			for(Track track:tracksFirst){
				s2 += track.getAuthor() + " - " + track.getName() + "\n";
			}
			
			s2 += "\nTracce secondo utente simile: \n";
			List<Track> tracksSecond = similarUserSecond.getTracksInTweets(tweets70Training);
			for(Track track:tracksSecond){
				s2 += track.getAuthor() + " - " + track.getName() + "\n";
			}
			
			s2 += "\nTracce suggerite utilizzando l'80% del database: \n";
			for(Track track:suggestedTracks){
				s2 += track.getAuthor() + " - " + track.getName() + "\n";
			}
			
			s2 += "\nTracce che ha sentito l'utente nel rimanente 20% del database: \n";
			for(Track track:tracks20Test){
				s2 += track.getAuthor() + " - " + track.getName() + "\n";
			}
			
			System.out.println(s2);
			out2.write(s2);
		}else{
			System.out.println(user.getName() + " - NON SIMILE A NESSUN ALTRO UTENTE");
			out2.write(user.getName() + " - NON SIMILE A NESSUN ALTRO UTENTE");
		}
		
		out1.close();
		out2.close();
    }
    
    private static List<String> union(List<String> list1, List<String> list2) {
        Set<String> set = new HashSet<String>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<String>(set);
    }

    private static List<String> intersection(List<String> list1, List<String> list2) {
        List<String> list = new ArrayList<String>();

        for (String t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
    
    private ArrayList<ArrayList<Double>> getSimilarityMatrix(EntityManager em, List<User> users, int size, double cAuthor, double cTrack){
    	ArrayList<ArrayList<Double>> similarityAuthor = new ArrayList<ArrayList<Double>>();
    	ArrayList<ArrayList<Double>> similarityTrack = new ArrayList<ArrayList<Double>>();
    	ArrayList<ArrayList<Double>> similarity = new ArrayList<ArrayList<Double>>();
    	
    	for(int i = 0; i<size; i++){
			javax.persistence.Query qUser1Author = em.createQuery("SELECT DISTINCT tr.author FROM Track tr, Tweet tw WHERE tw.user.id = '"+ users.get(i).getId() +"' AND tw.track = tr");
			List<String> trackUser1Author = (List<String>) qUser1Author.getResultList();
			
			javax.persistence.Query qUser1Track = em.createQuery("SELECT DISTINCT tr.name FROM Track tr, Tweet tw WHERE tw.user.id = '"+ users.get(i).getId() +"' AND tw.track = tr");
			List<String> trackUser1Track = (List<String>) qUser1Track.getResultList();
			
			ArrayList<Double> rowAuthor = new ArrayList<Double>();
			ArrayList<Double> rowTrack = new ArrayList<Double>();
			ArrayList<Double> rowSimilarity = new ArrayList<Double>();
			
			for(int j = 0; j<size; j++){
				javax.persistence.Query qUser2Author = em.createQuery("SELECT DISTINCT tr.author FROM Track tr, Tweet tw WHERE tw.user.id = '" + users.get(j).getId() + "' AND tw.track = tr");
				List<String> trackUser2Author = (List<String>) qUser2Author.getResultList();
				
				javax.persistence.Query qUser2Track = em.createQuery("SELECT DISTINCT tr.name FROM Track tr, Tweet tw WHERE tw.user.id = '" + users.get(j).getId() + "' AND tw.track = tr");
				List<String> trackUser2Track = (List<String>) qUser2Track.getResultList();
				
				List<String> unionAuthor = union(trackUser1Author, trackUser2Author);
				List<String> intersectionAuthor = intersection(trackUser1Author, trackUser2Author);
				double jaccardAuthor = (double)intersectionAuthor.size()/(double)unionAuthor.size();
				
				List<String> unionTrack = union(trackUser1Track, trackUser2Track);
				List<String> intersectionTrack = intersection(trackUser1Track, trackUser2Track);
				double jaccardTrack = (double)intersectionTrack.size()/(double)unionTrack.size();
				
				rowAuthor.add(jaccardAuthor);
				rowTrack.add(jaccardAuthor);
				rowSimilarity.add(cAuthor * jaccardAuthor + cTrack * jaccardTrack);
			}
			similarityAuthor.add(rowAuthor);
			similarityTrack.add(rowTrack);
			similarity.add(rowSimilarity);
			 
			System.out.println("Utente n° " + (i+1) + " di " + size);
		}
    	
    	return similarity;
    }
    
    private ArrayList<Double> getSimilarityVector(EntityManager em, User user, List<User> users, double cAuthor, double cTrack){
    	ArrayList<Double> similarityAuthor = new ArrayList<Double>();
    	ArrayList<Double> similarityTrack = new ArrayList<Double>();
    	ArrayList<Double> similarity = new ArrayList<Double>();
    	
    	javax.persistence.Query qUser1Author = em.createQuery("SELECT DISTINCT tr.author FROM Track tr, Tweet tw WHERE tw.user.id = '"+ user.getId() +"' AND tw.track = tr");
		List<String> trackUser1Author = (List<String>) qUser1Author.getResultList();
			
		javax.persistence.Query qUser1Track = em.createQuery("SELECT DISTINCT tr.name FROM Track tr, Tweet tw WHERE tw.user.id = '"+ user.getId() +"' AND tw.track = tr");
		List<String> trackUser1Track = (List<String>) qUser1Track.getResultList();
			
		for(int i = 0; i<users.size(); i++){
			javax.persistence.Query qUser2Author = em.createQuery("SELECT DISTINCT tr.author FROM Track tr, Tweet tw WHERE tw.user.id = '" + users.get(i).getId() + "' AND tw.track = tr");
			List<String> trackUser2Author = (List<String>) qUser2Author.getResultList();
				
			javax.persistence.Query qUser2Track = em.createQuery("SELECT DISTINCT tr.name FROM Track tr, Tweet tw WHERE tw.user.id = '" + users.get(i).getId() + "' AND tw.track = tr");
			List<String> trackUser2Track = (List<String>) qUser2Track.getResultList();
				
			List<String> unionAuthor = union(trackUser1Author, trackUser2Author);
			List<String> intersectionAuthor = intersection(trackUser1Author, trackUser2Author);
			double jaccardAuthor = (double)intersectionAuthor.size()/(double)unionAuthor.size();
				
			List<String> unionTrack = union(trackUser1Track, trackUser2Track);
			List<String> intersectionTrack = intersection(trackUser1Track, trackUser2Track);
			double jaccardTrack = (double)intersectionTrack.size()/(double)unionTrack.size();
				
			similarityAuthor.add(jaccardAuthor);
			similarityTrack.add(jaccardAuthor);
			similarity.add(cAuthor * jaccardAuthor + cTrack * jaccardTrack);
			
			if(i%100 == 0)
				System.out.println("Analizzati " + i + " utenti su " + users.size());
		}
		
		System.out.println("Analizzati " + users.size() + " utenti su " + users.size());
		
    	return similarity;
    }
    
    private ArrayList<Double> getSimilarityVectorFromTweets(EntityManager em, User user, List<User> users, List<Tweet> tweets, double cAuthor, double cTrack){
    	ArrayList<Double> similarityAuthor = new ArrayList<Double>();
    	ArrayList<Double> similarityTrack = new ArrayList<Double>();
    	ArrayList<Double> similarity = new ArrayList<Double>();
    	
    	List<Track> tracksUser1 = user.getTracksInTweets(tweets);

		List<String> trackUser1Author = getAuthorFromTracks(tracksUser1);
		List<String> trackUser1Track = getNameFromTracks(tracksUser1);
			
		for(int i = 0; i<users.size(); i++){
			List<Track> tracksUser2 = users.get(i).getTracksInTweets(tweets);

			List<String> trackUser2Author = getAuthorFromTracks(tracksUser2);
			List<String> trackUser2Track = getNameFromTracks(tracksUser2);
				
			List<String> unionAuthor = union(trackUser1Author, trackUser2Author);
			List<String> intersectionAuthor = intersection(trackUser1Author, trackUser2Author);
			double jaccardAuthor = (double)intersectionAuthor.size()/(double)unionAuthor.size();
				
			List<String> unionTrack = union(trackUser1Track, trackUser2Track);
			List<String> intersectionTrack = intersection(trackUser1Track, trackUser2Track);
			double jaccardTrack = (double)intersectionTrack.size()/(double)unionTrack.size();
				
			similarityAuthor.add(jaccardAuthor);
			similarityTrack.add(jaccardAuthor);
			similarity.add(cAuthor * jaccardAuthor + cTrack * jaccardTrack);
			
			if(i%100 == 0)
				System.out.println("Analizzati " + i + " utenti su " + users.size());
		}
		
		System.out.println("Analizzati " + users.size() + " utenti su " + users.size());
		
    	return similarity;
    }
   
    private List<Track> getListSuggestedTracksFromTweets(User user, User similarUser1, User similarUser2,List<Tweet> tweets) {
    	List<Track> tracksSimilarUser1 = similarUser1.getTracksInTweets(tweets);
    	List<Track> tracksSimilarUser2 = similarUser2.getTracksInTweets(tweets);
    	List<Track> tracksUser = user.getTracksInTweets(tweets);
    	
    	for(Track track: tracksUser){
    		tracksSimilarUser1.remove(track);
    		tracksSimilarUser2.remove(track);
    	}
    	
    	for(Track track: tracksSimilarUser1){
    		tracksSimilarUser2.remove(track);
    	}
    	
    	tracksSimilarUser1.addAll(tracksSimilarUser2);
    	return tracksSimilarUser1;
    }
    
    private List<Track> getListSuggestedTracks(User user, User similarUser1, User similarUser2) {
    	List<Track> tracksSimilarUser1 = similarUser1.getTracks();
    	List<Track> tracksSimilarUser2 = similarUser2.getTracks();
    	List<Track> tracksUser = user.getTracks();
    	
    	for(Track track: tracksUser){
    		tracksSimilarUser1.remove(track);
    		tracksSimilarUser2.remove(track);
    	}
    	
    	for(Track track: tracksSimilarUser1){
    		tracksSimilarUser2.remove(track);
    	}
    	
    	tracksSimilarUser1.addAll(tracksSimilarUser2);
    	return tracksSimilarUser1;
    }
    
    private List<String> getAuthorFromTracks(List<Track> tracks){
    	List<String> trackUserAuthor = new ArrayList<String>();
    	
    	for(Track track:tracks){
    		if(!trackUserAuthor.contains(track.getAuthor()))
    			trackUserAuthor.add(track.getAuthor());
    	}
    	
    	return trackUserAuthor;
    }
    
    private List<String> getNameFromTracks(List<Track> tracks){
    	List<String> trackUserName = new ArrayList<String>();
    	
    	for(Track track:tracks){
    		if(!trackUserName.contains(track.getName()))
    			trackUserName.add(track.getName());
    	}
    	
    	return trackUserName;
    }
}
