/*
* Title： SplitHASCCorpus_into_TerminalPosition
* 説明 ： HASCデータを「端末の位置(TerminalPosition)別に分割して出力するプログラム
* @date Created on: 2016/06/21
* @author Author: Haruyuki Ichino
* @version 1.0
*
*/


import java.io.*;
import java.nio.file.*;
import java.util.StringTokenizer;

public class SplitHASCCorpus_into_TerminalPosition {

    // データの場所指定
    static String data_path = "./data/";
    // 出力データの格納場所
    static String output_path = "./output/";


    public static void main(String[] args){

        // もし出力フォルダがなければ作成
        File output_dir = new File(output_path);
        if(output_dir.exists() == false){
            output_dir.mkdir();
        }

        // 通常のファイル(隠しファイルでない)のみを取り出すフィルタの作成
        FilenameFilter normalFileFilter = new FilenameFilter() {
            public boolean accept(File file, String name) {
                if (file.isHidden() == false){
                    return true;
                } else {
                    return false;
                }
            }
        };
        // metaファイルのみを取り出すフィルタ
        FilenameFilter metaFileFilter = new FilenameFilter() {
            public boolean accept(File file, String name) {
                if (name.matches(".*meta.*")){
                    return true;
                } else {
                    return false;
                }
            }
        };


        System.out.println("========================================================================");
        System.out.println("1.ファイルの読み込み");
        System.out.println("========================================================================");
        File data_dir = new File(data_path);

        float meta_file_count = 0;
        float good_meta_file_count = 0;

        // data内のファイルを取得
        File[] activity_dirs = data_dir.listFiles(normalFileFilter);

        System.out.println("Activity count = " + activity_dirs.length);

        // 各行動ディレクトリにアクセス
        for (File activity_dir : activity_dirs){
            if(activity_dir.isHidden() == false){
                System.out.println("===================================================");
                System.out.println(activity_dir);
                System.out.println("===================================================");

                // 行動ディレクトリ内のファイルを取得
                File[] person_dirs = activity_dir.listFiles(normalFileFilter);

                System.out.println("person count = " + person_dirs.length);

                // 各personディレクトリにアクセス
                for(File person_dir : person_dirs){
                    if(person_dir.isHidden() == false){
                        System.out.println("======================================");
                        System.out.println(person_dir.getName());
                        System.out.println("======================================");

                        // personディレクトリ内のファイルを取得
                        File[] files = person_dir.listFiles(normalFileFilter);
                        File[] meta_files = person_dir.listFiles(metaFileFilter);

//                        System.out.println("meta files count = " + meta_files.length);
//                        System.out.println();

                        // 各metaファイルにアクセス
                        for(File meta_file : meta_files){
                            meta_file_count++;

                            String meta_file_name = meta_file.getName();
                            System.out.println(meta_file_name);

                            // 名前からID部分の取り出し
                            int idx_hascID = meta_file_name.indexOf(".");
                            String file_id = meta_file_name.substring(0,idx_hascID);

                            // メタファイルからTerminalPositionの取り出し
                            String termialPosition = getTerminalPosition(meta_file);

                            // 出力ディレクトリにTerminalPositionのディレクトリを作成
                            File output_terminalPosition_dir = new File(output_path+termialPosition+"/");
                            if(output_terminalPosition_dir.exists() == false){
                                output_terminalPosition_dir.mkdir();
                            }

                            // 出力ディレクトリに行動ディレクトリを作成
                            File output_activity_dir = new File(output_terminalPosition_dir.getPath()+"/"+activity_dir.getName()+"/");
                            if(output_activity_dir.exists() == false){
                                output_activity_dir.mkdir();
                            }
                            // 出力ディレクトリにpersonディレクトリを作成
                            File output_person_dir = new File(output_activity_dir.getPath()+"/"+person_dir.getName()+"/");
                            if(output_person_dir.exists() == false){
                                output_person_dir.mkdir();
                            }


                            // 入力ファイルのパス設定
                            Path accFilePath = FileSystems.getDefault().getPath(person_dir.getPath()+"/"+file_id+"-acc.csv");
                            Path gyroFilePath = FileSystems.getDefault().getPath(person_dir.getPath()+"/"+file_id+"-gyro.csv");
                            Path magFilePath = FileSystems.getDefault().getPath(person_dir.getPath()+"/"+file_id+"-mag.csv");
                            Path metaFilePath = FileSystems.getDefault().getPath(person_dir.getPath()+"/"+file_id+".meta");
                            Path labelFilePath = FileSystems.getDefault().getPath(person_dir.getPath()+"/"+file_id+".label");

                            // 出力ファイルのパス設定
                            Path output_accFilePath = FileSystems.getDefault().getPath(output_person_dir.getPath()+"/"+file_id+"-acc.csv");
                            Path output_gyroFilePath = FileSystems.getDefault().getPath(output_person_dir.getPath()+"/"+file_id+"-gyro.csv");
                            Path output_magFilePath = FileSystems.getDefault().getPath(output_person_dir.getPath()+"/"+file_id+"-mag.csv");
                            Path output_metaFilePath = FileSystems.getDefault().getPath(output_person_dir.getPath()+"/"+file_id+".meta");
                            Path output_labelFilePath = FileSystems.getDefault().getPath(output_person_dir.getPath()+"/"+file_id+".label");

                            // コピー処理
                            // 加速度
                            try {
                                Files.copy(accFilePath, output_accFilePath);
                                System.out.println("コピー完了: "+output_accFilePath.toString());
                            } catch (FileAlreadyExistsException e){
                                System.out.println("\tError: ファイルがすでに存在しています!");
                            } catch (NoSuchFileException e) {
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // 角速度
                            try {
                                Files.copy(gyroFilePath, output_gyroFilePath);
                                System.out.println("コピー完了: "+output_gyroFilePath.toString());
                            } catch (FileAlreadyExistsException e){
                                System.out.println("\tError: ファイルがすでに存在しています!");
                            } catch (NoSuchFileException e) {
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // 地磁気
                            try {
                                Files.copy(magFilePath, output_magFilePath);
                                System.out.println("コピー完了: "+output_magFilePath.toString());
                            } catch (FileAlreadyExistsException e){
                                System.out.println("\tError: ファイルがすでに存在しています!");
                            } catch (NoSuchFileException e) {
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // meta
                            try {
                                Files.copy(metaFilePath, output_metaFilePath);
                                System.out.println("コピー完了: "+output_metaFilePath.toString());
                            } catch (FileAlreadyExistsException e){
                                System.out.println("\tError: ファイルがすでに存在しています!");
                            } catch (NoSuchFileException e) {
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // label
                            try {
                                Files.copy(labelFilePath, output_labelFilePath);
                                System.out.println("コピー完了: "+output_labelFilePath.toString());
                            } catch (FileAlreadyExistsException e){
                                System.out.println("\tError: ファイルがすでに存在しています!");
                            } catch (NoSuchFileException e) {
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        // 使えるデータの割合の表示
//        System.out.println();
//        String good_data_rate = String.format("%.2f", good_meta_file_count/meta_file_count*100);
//        System.out.println("条件を満たすHASCデータセットの割合: " + good_data_rate + "% (" + String.valueOf((int)good_meta_file_count) + "/" + String.valueOf((int)meta_file_count) +")");
    }



    static String getTerminalPosition(File file){

        try {
            //ファイルを読み込む
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            //読み込んだファイルを１行ずつ処理する
            String line_str;
            StringTokenizer token;
            for(int i=0; (line_str = br.readLine()) != null; i++){

                // TerminalPositionの処理
                if (line_str.indexOf("TerminalPosition") != -1) {

                    // TerminalPositionから内容の取り出し
                    int idx_position = line_str.indexOf(":") + 1;
                    String position = line_str.substring(idx_position, line_str.length());
                    position = position.trim(); // 空白の削除

                    System.out.println("\t"+position);

                    return position;
                }
            }

            //終了処理
            br.close();

        } catch (IOException ex) {
            //例外発生時処理
            ex.printStackTrace();
        }

        return "unknown";
    }

}

