package wseemann.media.jplaylistparser;

public abstract class PlayListAbstractClass {
	
	private ListType ListType=null;

	


	
	/**
	 * @return the listType
	 */
	public ListType getListType() {
		return ListType;
	}





	/**
	 * @param listType the listType to set
	 */
	public void setListType(ListType listType) {
		ListType = listType;
	}





	public PlayListAbstractClass(ListType  ListType){
		this.ListType=ListType;
		
		
		
	}
	
	
	

}
