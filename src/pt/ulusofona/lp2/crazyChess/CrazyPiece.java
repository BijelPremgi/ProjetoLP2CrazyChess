package pt.ulusofona.lp2.crazyChess;

public class CrazyPiece {
    int id;
    int tipo = 0;
    int idEquipa;
    String alcunha;
    int x;
    int y;

    public String toString(){
        return "id: " + id + "|| idEquipa: " + idEquipa + "|| Equipa: " + idEquipa + "|| Alcunha: " + alcunha
                + "|| X:" + x + "|| Y:" + y;
    }
    public CrazyPiece(int id, int tipo, int idEquipa, String alcunha){
        this.id = id;
        this.tipo = tipo;
        this.idEquipa = idEquipa;
        this.alcunha = alcunha;
    }
    public void setPieceCoord(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getId(){
        return id;
    }
    public int getIdEquipa(){
        return idEquipa;
    }
    public String getImagePNG(){
        return "img.png";
    }
}