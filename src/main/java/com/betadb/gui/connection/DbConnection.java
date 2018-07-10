package com.betadb.gui.connection;

import com.betadb.gui.dbobjects.Server;

/**
 * @author parmstrong
 */
public class DbConnection
{
    private final Server server;
    private final String startingSql;
    private final String selectedDb;

    public DbConnection(Server server, String selectedDb)
    {
        this(server, "", selectedDb);
    }

    public DbConnection(Server server, String startingSql, String selectedDb)
    {
        this.server = server;
        this.startingSql = "";
        this.selectedDb = selectedDb;
    }

    public Server getServer()
    {
        return server;
    }

    public String getStartingSql()
    {
        return startingSql;
    }

    public String getSelectedDb()
    {
        return selectedDb;
    }

}
