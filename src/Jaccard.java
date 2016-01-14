import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Jaccard {
	public static void main(String[] args) throws IOException {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("projectSII");
		EntityManager em = emf.createEntityManager();
		JaccardSimilarity jaccard = new JaccardSimilarity();
		
		String idUser = "13229";
		
		jaccard.runOneUser60percent(em, idUser);
	}
}

