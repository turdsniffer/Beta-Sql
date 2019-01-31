package com.betadb.gui.dbobjects;

import com.betadb.gui.dao.DbInfoDAO;
import com.google.common.collect.Lists;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author parmstrong
 */
public class DbInfo extends DbObject
{
	private DbInfoDAO dbInfoDAO;
    private List<Schema> schemas;
    private boolean loaded;

    public DbInfo(String dbName, DbInfoDAO dbInfoDAO)
	{
		this.name = dbName;
        this.dbInfoDAO = dbInfoDAO;
        schemas = Lists.newArrayList();
	}	

	public List<DbObject> getAllDbObjects()
	{
		List<DbObject> retVal = new ArrayList<>();
        retVal.add(this);
		retVal.addAll(this.getTables());
		retVal.addAll(this.getViews());
		retVal.addAll(this.getFunctions());
		retVal.addAll(this.getProcedures());
        retVal.addAll(this.getSchemas());
		return retVal;
	}

    //This method will refresh this dbinfo back to a default state with only the default schema loaded
    public void refreshToDefault() throws SQLException{
        this.getDbInfoDAO().refreshDbInfo(this);
    }
	
	@Override
	public String toString()
	{
		return name;
	}

    public void loadSchemaInfo(String schemaName) throws SQLException{
        Optional<Schema> schema = this.schemas.stream().filter(s -> s.getName().equalsIgnoreCase(schemaName)).findFirst();
        if(schema.isPresent() && !schema.get().isLoaded())
            this.dbInfoDAO.loadSchemaInfo(this, schema.get());
    }

    public void load() throws SQLException{
        if(!loaded)
        {    
            dbInfoDAO.refreshDbInfo(this);
            this.loaded = true;
        }
    }
    
    
    public DbInfoDAO getDbInfoDAO()
    {
        return dbInfoDAO;
    }
    

    public List<Table> getTables()
	{
		return this.schemas.stream().flatMap(schema -> schema.getTables().stream()).collect(Collectors.toList());
    }

	public List<Procedure> getProcedures()
	{        
		return this.schemas.stream().flatMap(schema -> schema.getProcedures().stream()).collect(Collectors.toList());
	}

	public List<View> getViews()
	{        
		return this.schemas.stream().flatMap(schema -> schema.getViews().stream()).collect(Collectors.toList());
	}

	public List<Function> getFunctions()
	{
		return this.schemas.stream().flatMap(schema -> schema.getFunctions().stream()).collect(Collectors.toList());
	}

	public Schema getDefaultSchema()
	{
		return schemas.stream().filter(schema -> schema.getName().equalsIgnoreCase("dbo")).findFirst().get();
	}
    
    public List<Schema> getSchemas()
    {
        return this.schemas;
    }

    public void setSchemas(List<Schema> schemas)
    {
        this.schemas = schemas;
    }
    

	@Override
	public int hashCode()
	{
		int hash = 3;
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DbInfo other = (DbInfo) obj;
		if (!Objects.equals(this.name, other.name))
			return false;
		return true;
	}


	
	
}
