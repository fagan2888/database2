/*@entity type
Acthor
*/
public class Actor{
	String [] FilmName;
	String [] Character;
	
	//using string[] to get string[]
	public void FilmsParticipated(String[] character,String[] filmname){
		for (int i=0;i< character.length;i++){
			Character[i] = character[i];
		    FilmName[i] = filmname[i];
		}
	}
	
	public void print(){
		System.out.println("---------"); //still not sure about this part
	}
	
}