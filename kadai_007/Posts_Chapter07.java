package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Posts_Chapter07 {

	public static void main(String[] args) {
		
        Connection con = null;
        PreparedStatement statement = null;

        // ユーザーリスト
        String[][] userList = {
            { "1003","2023-02-08", "昨日の夜は徹夜でした・・", "13"},
            { "1002","2023-02-08", "お疲れ様です！", "12"},
            { "1003","2023-02-09", "今日も頑張ります！", "18"},
            { "1001","2023-02-09", "無理は禁物ですよ！", "17"},
            { "1002","2023-02-10", "明日から連休ですね！", "20"}
        };

        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/challenge_java",
                "root",
                "MySQL_SAMURAI00"
            );

            System.out.println("データベース接続成功:"+con);  //出力1

            // SQLクエリを準備
            String sql = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?);";
            statement = con.prepareStatement(sql);

            System.out.println("レコード追加を実行します");  //出力2
            int totalCnt = 0;
            // リストの1行目から順番に読み込む
            int rowCnt = 0;
            for( int i = 0; i < userList.length; i++ ) {
                // SQLクエリの「?」部分をリストのデータに置き換え
                statement.setString(1, userList[i][0]); // user_id
                statement.setString(2, userList[i][1]); // posted_at
                statement.setString(3, userList[i][2]); // post_content
                statement.setString(4, userList[i][3]); // likes

                // SQLクエリを実行（DBMSに送信）
                rowCnt = statement.executeUpdate();
                totalCnt += rowCnt;
            }
            //rowCnt = statement.executeUpdate();
            System.out.println( totalCnt + "件のレコードが追加されました"); //出力3
            
            // SQLクエリを準備
            sql = "SELECT user_id, posted_at, post_content, likes FROM posts WHERE user_id = 1002;";
            //statement = con.prepareStatement(sql);
            
            //　SQLクエリを実行（DBMSに送信）
            ResultSet result = statement.executeQuery(sql);

            // SQLクエリの実行結果を抽出
            while(result.next()) {
                String posted_at = result.getString("posted_at");
                String post_content = result.getString("post_content");
                int likes = result.getInt("likes");
                System.out.println(result.getRow() + "件目：投稿日時=" + posted_at + "／投稿内容=" + post_content  + "／いいね数=" + likes );
            }
            
        } catch(SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } finally {
            // 使用したオブジェクトを解放
            if( statement != null ) {
                try { statement.close(); } catch(SQLException ignore) {}
            }
            if( con != null ) {
                try { con.close(); } catch(SQLException ignore) {}
            }
        }

	}

}
