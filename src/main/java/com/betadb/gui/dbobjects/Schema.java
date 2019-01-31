package com.betadb.gui.dbobjects;

import com.betadb.gui.dao.DbInfoDAO;
import com.google.common.collect.Lists;
import java.util.List;

public class Schema extends DbObject
{
    private List<Table> tables;
    private List<Procedure> procedures;
    private List<View> views;
    private List<Function> functions;
    boolean loaded;
    private DbInfoDAO dbInfoDAO;
    private DbInfo dbInfo;

    public Schema(String name, DbInfoDAO dbInfoDAO, DbInfo dbInfo)
    {
        this.name = name;
        tables = Lists.newArrayList();
        procedures = Lists.newArrayList();
        views = Lists.newArrayList();
        functions = Lists.newArrayList();
        this.dbInfo = dbInfo;
        this.dbInfoDAO = dbInfoDAO;
    }

    public List<Table> getTables()
    {
        return this.tables;
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public void setLoaded(boolean loaded)
    {
        this.loaded = loaded;
    }
    
    public void load()
    {
        dbInfoDAO.loadSchemaInfo(this.dbInfo, this);
    }

    public void setTables(List<Table> tables)
    {
        this.tables = tables;
    }

    public List<Procedure> getProcedures()
    {
        return this.procedures;
    }

    public void setProcedures(List<Procedure> procedures)
    {
        this.procedures = procedures;
    }

    public List<View> getViews()
    {
        return this.views;
    }

    public void setViews(List<View> views)
    {
        this.views = views;
    }

    public List<Function> getFunctions()
    {
        return this.functions;
    }

    public void setFunctions(List<Function> functions)
    {
        this.functions = functions;
    }

}
