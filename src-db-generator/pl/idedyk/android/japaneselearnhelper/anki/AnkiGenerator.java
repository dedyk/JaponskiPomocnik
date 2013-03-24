package pl.idedyk.android.japaneselearnhelper.anki;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AnkiGenerator {

	public static void main(String[] args) throws Exception {
		
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;
		Statement statement = null;
		
		final String dbOutputFilePath = "ankitest/ankitest.db";
		
		new File(dbOutputFilePath).delete();
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbOutputFilePath);
			
			statement = connection.createStatement();
			
			createTables(statement);
			
			String crt = String.valueOf(intNow());
			String mod = String.valueOf(intNow(1000));
			String scm = String.valueOf(intNow(1000));
			
			String conf = generateColConf();
			
			// FIXME: dokonczyc, ale jak?
			
			
			System.out.println(crt + " " + mod + " " + scm + " " + conf);

		} finally {
			
			if (statement != null) {
				statement.close();
			}
			
			if (connection != null) {
				connection.close();
			}			
		}
	}

	private static void createTables(Statement statement) throws SQLException {
		
		for (String currentCreateSql : createTableSql) {
			statement.execute(currentCreateSql);
		}
	}
	
	private static String[] createTableSql = new String[] { 
		"create table if not exists col (\n" +
		"	    id              integer primary key,\n" +
		"	    crt             integer not null,\n" +
		"	    mod             integer not null,\n" +
		"	    scm             integer not null,\n" +
		"	    ver             integer not null,\n" +
		"	    dty             integer not null,\n" +
		"	    usn             integer not null,\n" +
		"	    ls              integer not null,\n" +
		"	    conf            text not null,\n" +
		"	    models          text not null,\n" +
		"	    decks           text not null,\n" +
		"	    dconf           text not null,\n" +
		"	    tags            text not null\n" +
		"	);\n",
	
		"create table if not exists notes (\n" +
		"	    id              integer primary key,\n" +
		"	    guid            text not null,\n" +
		"	    mid             integer not null,\n" +
		"	    mod             integer not null,\n" +
		"	    usn             integer not null,\n" +
		"	    tags            text not null,\n" +
		"	    flds            text not null,\n" +
		"	    sfld            integer not null,\n" +
		"	    csum            integer not null,\n" +
		"	    flags           integer not null,\n" +
		"	    data            text not null\n" +
		"	);\n",
	
		"create table if not exists cards (\n" +
		"	    id              integer primary key,\n" +
		"	    nid             integer not null,\n" +
		"	    did             integer not null,\n" +
		"	    ord             integer not null,\n" +
		"	    mod             integer not null,\n" +
		"	    usn             integer not null,\n" +
		"	    type            integer not null,\n" +
		"	    queue           integer not null,\n" +
		"	    due             integer not null,\n" +
		"	    ivl             integer not null,\n" +
		"	    factor          integer not null,\n" +
		"	    reps            integer not null,\n" +
		"	    lapses          integer not null,\n" +
		"	    left            integer not null,\n" +
		"	    odue            integer not null,\n" +
		"	    odid            integer not null,\n" +
		"	    flags           integer not null,\n" +
		"	    data            text not null\n" +
		"	);\n",
	
		"create table if not exists revlog (\n" +
		"	    id              integer primary key,\n" +
		"	    cid             integer not null,\n" +
		"	    usn             integer not null,\n" +
		"	    ease            integer not null,\n" +
		"	    ivl             integer not null,\n" +
		"	    lastIvl         integer not null,\n" +
		"	    factor          integer not null,\n" +
		"	    time            integer not null,\n" +
		"	    type            integer not null\n" +
		"	);\n",
	
		"create table if not exists graves (\n" +
		"	    usn             integer not null,\n" +
		"	    oid             integer not null,\n" +
		"	    type            integer not null\n" +
		"	);\n"
	};
	
    /**The time in integer seconds. Pass scale=1000 to get milliseconds. */
    public static double now() {
        return (System.currentTimeMillis() / 1000.0);
    }

    /**The time in integer seconds. Pass scale=1000 to get milliseconds. */
    public static long intNow() {
    	return intNow(1);
    }
    
	public static long intNow(int scale) {
        return (long) (now() * scale);
    }
	
	private static String generateColConf() {		
		return String.format(colConf, intNow(1000));
	}
	
	private static String colConf = 
		"{\n" +
		"   \"estTimes\":true,\n" +
		"   \"timeLim\":0,\n" +
		"   \"nextPos\":1,\n" +
		"   \"activeDecks\":[\n" +
		"      1\n" +
		"   ],\n" +
		"   \"addToCur\":true,\n" +
		"   \"curModel\":%d,\n" +
		"   \"dueCounts\":true,\n" +
		"   \"sortBackwards\":false,\n" +
		"   \"sortType\":\"noteFld\",\n" +
		"   \"nextPOS\":3,\n" +
		"   \"newSpread\":0,\n" +
		"   \"curDeck\":\"1\",\n" +
		"   \"collapseTime\":1200\n" +
		"}";
}
