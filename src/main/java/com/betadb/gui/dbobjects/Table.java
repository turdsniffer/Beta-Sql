package com.betadb.gui.dbobjects;

import com.betadb.gui.dao.DbInfoDAO;
import com.swingautocompletion.main.AutoCompleteItem;
import java.util.ArrayList;
import java.util.List;

/**
 * @author parmstrong
 */
public class Table extends DbObject
{
    private DbInfoDAO dbInfoDAO;
    private List<Column> columns;
    private List<Index> indexes;
    private List<ForeignKey> foreignKeys;
    private List<PrimaryKey> primaryKeys;

    public Table(DbInfoDAO dbInfoDAO)
    {
        this.dbInfoDAO = dbInfoDAO;
    }

    public List<Column> getColumns()
    {
        if (columns == null)
            this.columns = dbInfoDAO.getColumns(this);
        return columns;
    }

    @Override
    public List<? extends AutoCompleteItem> getSubSuggestions()
    {
        return new ArrayList<>(getColumns());
    }

    public List<Index> getIndexes()
    {
        if (indexes == null)
            this.indexes = dbInfoDAO.getIndexes(this);
        return indexes;
    }



    public List<ForeignKey> getForeignKeys()
    {
        if (foreignKeys == null)
            this.foreignKeys = dbInfoDAO.getForeignKeys(this);

        return foreignKeys;
    }

    public List<PrimaryKey> getPrimaryKeys()
    {
        if (primaryKeys == null)
            this.primaryKeys = dbInfoDAO.getPrimaryKeys(this);
        return primaryKeys;
    }

}
