package at.redeye.SqlDBInterface.SqlDBIO.impl;

public class ColumnAttribute {

	private boolean primaryKey = false;

	private boolean hasIndex = false;

	private DBDataType datatype;

	private int width = 0;

	public ColumnAttribute(DBDataType datatype) {

		super();

		this.datatype = datatype;

	}

	public ColumnAttribute(boolean primaryKey,

	DBDataType datatype) {

		super();

		this.primaryKey = primaryKey;

		this.datatype = datatype;

	}

	public ColumnAttribute(boolean primaryKey, DBDataType datatype, int width) {	

		this.primaryKey = primaryKey;
		this.datatype = datatype;        
        this.width = width;

	}    
    
    public ColumnAttribute( DBDataType datatype, int width) {	
		
		this.datatype = datatype;        
        this.width = width;

	}    
    
    
	/**
	 * 
	 * @return the primaryKey
	 */

	public boolean isPrimaryKey() {

		return primaryKey;

	}

	/**
	 * 
	 * @param primaryKey
	 *            the primaryKey to set
	 */

	public void setPrimaryKey(boolean primaryKey) {

		this.primaryKey = primaryKey;

	}

	/**
	 * 
	 * @return the datatype
	 */

	public DBDataType getDatatype() {

		return datatype;

	}

	/**
	 * 
	 * @param datatype
	 *            the datatype to set
	 */

	public void setDatatype(DBDataType datatype) {

		this.datatype = datatype;

	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public boolean hasIndex() {
		return hasIndex;
	}

	public void setHasIndex(boolean value) {
		hasIndex = value;
	}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.primaryKey ? 1 : 0);
        hash = 67 * hash + (this.hasIndex ? 1 : 0);
        hash = 67 * hash + (this.datatype != null ? this.datatype.hashCode() : 0);
        hash = 67 * hash + this.width;
        return hash;
    }
    
    @Override
    public boolean equals( Object other )
    {
        if( other == null )
            return false;
        
        if( other == this )
            return true;
        
        if( other instanceof ColumnAttribute )
        {
            ColumnAttribute attr = (ColumnAttribute)other;
            
            if( this.hasIndex != attr.hasIndex )
                return false;
            
            if( this.primaryKey != attr.primaryKey )
                return false;            
            
            if( this.width != attr.width )
                return false;            
            
            if( this.datatype != attr.datatype )
                return false;
            
            return true;            
            
        } 
            
        return false;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append(datatype);
        
        if( width > 0 ) {
            sb.append("(");
            sb.append(width);
            sb.append(")");
        }
        
        if( this.primaryKey  )
            sb.append( " Primary");
        
        if( this.hasIndex )
            sb.append( " Index");               
        
        return sb.toString();
    }

}
