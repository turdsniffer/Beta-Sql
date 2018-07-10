package com.betadb.gui.dbobjects;

import com.betadb.gui.dao.DbInfoDAO;
import com.betadb.gui.datasource.DataSourceKey;
import com.betadb.gui.exception.BetaDbException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author parmstrong
 */
public class Server
{
    private List<DbInfo> dbs;
    private String serverName;
    private final String instanceName;

    private final String db;
    private final DbInfoDAO dbInfoDAO;

    public Server(String serverName, String instanceName, String db, DbInfoDAO dbInfoDAO)
    {
        this.serverName = serverName;
        this.db = db;
        this.instanceName = instanceName;
        this.dbInfoDAO = dbInfoDAO;
    }

    public String getInstanceName()
    {
        return instanceName;
    }

    /**
     * @return the dbs
     * @throws java.sql.SQLException
     */
    public List<DbInfo> getDbs()
    {
        try
        {
            if (dbs == null)
                dbs = dbInfoDAO.getDatabases();

            return dbs;
        }
        catch (SQLException ex)
        {
            throw new BetaDbException("error getting dbs", ex);
        }
    }

    public DbInfo getDb(String dbName)
    {
        return this.dbs.stream().filter(d -> d.getName().equalsIgnoreCase(dbName)).findFirst().get();
    }

    public DataSourceKey getDataSourceKey()
    {
        return new DataSourceKey(serverName, instanceName, db);
    }


    /**
     * @return the serverName
     */
    public String getServerName()
    {
        return serverName;
    }

    @Override
    public String toString()
    {
        String key = serverName;
        if (db != null && db.length() > 0)
            key += "/" + db;
        if (instanceName != null && instanceName.length() > 0)
            key += "/" + instanceName;
        return key;
    }

    public List<DbObject> getAllDbObjects()
    {
        return dbs.stream().flatMap(d -> d.getAllDbObjects().stream()).collect(Collectors.toList());
    }
}
