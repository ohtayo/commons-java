package jp.ohtayo.commons.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Accessor for SQLite3 database.
 *
 * @author ohtayo <ohta.yoshihiro@outlook.jp>
 */
public class SQLite {

	private String dbName;
	private Statement statement = null;
	private Connection connection = null;
	public ResultSet resultSet = null;

	/**
	 * コンストラクタ<br>
	 * DB名を指定してコネクションを張る<br>
	 * @param fileName DBのファイル名
	 */
	public SQLite(String fileName)
	{
		dbName = fileName;
		open();
	}

	/**
	 * ファイナライザ<br>
	 * クラスをGCで開放するときに必ずDBのコネクションを閉じる。<br>
	 */
	@Override
	protected void finalize() throws Throwable
	{
		try {
			close();
		} finally {
			super.finalize();
		}
	}

	/**
	 * DBにアクセスしてコネクションを開く<br>
	 */
	public void open()
	{
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:"+dbName);
			statement = connection.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			close();
		}
	}

	/**
	 * DBのコネクションを閉じる<br>
	 */
	public void close()
	{
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 開いているDBに対しクエリを実行してResultを保持<br>
	 * @param query
	 */
	public void executeSingleQuery(String query)
	{
		try {
			resultSet = statement.executeQuery(query);
			// for debug
			/*
			while (resultSet.next()) {
				System.out.println(resultSet.getString(1));
			}
			*/
		} catch (SQLException e) {
			e.printStackTrace();
			close();
		}
	}

	/**
	 * ResultSetの1列目のすべてのデータをString配列で取得
	 * @return データのString配列
	 */
	public String[] getStringData(int columnIndex)
	{
		//データをString配列に取得
		ArrayList<String> stringList = new ArrayList<String>();
		try {
			while( resultSet.next() ){
				stringList.add(resultSet.getString(columnIndex));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			close();
		}
		return (String[]) stringList.toArray(new String[0]);
	}
}
