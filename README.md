# RenameFilesRefactorBatch-intellij

This is an IntelliJ-based plugin that adds an action to batch replace the names of files in a directory selected in the project view.

The renaming uses the standard IntelliJ refactor function, so if the source file is in an object-oriented language, it will automatically replace the corresponding class name, etc. in the source code.

Operation confirmed at Java source files.

## How to run

Right-click on the directory in the Project View > `Refactor` > `Bulk Rename`

## Specification

- Do not change the directory name.
- Files in subdirectories are also searchable.
- Multiple selection of search directories is not supported.
- If more than one class is written in one source file, only the file name will be renamed.
- If the class name written in the source code is different from the file name, only the file name will be renamed.

---

プロジェクトビューで選択したディレクトリ内のファイルの名前を一括置換するアクションを追加するIntelliJ IDEAプラグインです。

名前変更にはIntelliJ標準のリファクタ機能を使用しているので、オブジェクト指向言語のソースファイルならソースコード上の対応するクラス名なども自動で置換してくれます。

Javaのソースファイルで動作確認済みです。

## 仕様 

- ディレクトリ名は変更しない
- サブディレクトリ内のファイルも検索範囲とする
- 検索ディレクトリの複数選択は非対応
- １つのソースファイルに2つ以上のクラスが書かれているファイルはファイル名だけのリネームが行われる
- ソースファイルに書いてあるクラス名とファイル名が異なる場合は、ファイル名だけのリネームが行われる

## 実行方法

プロジェクトビューでディレクトリを選択して右クリック > `Refactor` > `Bulk Rename`

