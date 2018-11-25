package pt.ulusofona.lp2.crazyChess;

import java.util.ArrayList;
import java.util.List;

public class Jogador {
    int idEquipa;
    List<CrazyPiece> listaPecas = new ArrayList<CrazyPiece>();
    int capturadas = 0;
    int jogadasValidas = 0;
    int tentativasInvalidas = 0;

    Jogador(int idEquipa){
        this.idEquipa = idEquipa;
    }
    public void addPeca(CrazyPiece piece){
        listaPecas.add(piece);
    }
    public void removePeca(CrazyPiece piece){ listaPecas.remove(piece); }
    public void incrementaCapuradas(){
        capturadas++;
    }
    public void incrementaJogadasValidas(){
        jogadasValidas++;
    }
    public void incrementaTentativasInvalidas(){
        tentativasInvalidas++;
    }
    // false -> se estiver vazia
    public int listaComprimento(){
        return listaPecas.size();
    }
    public List<Integer> getEstatisticas(){
        List<Integer> estatisticas = new ArrayList<Integer>();
        estatisticas.add(capturadas);
        estatisticas.add(jogadasValidas);
        estatisticas.add(tentativasInvalidas);

        return estatisticas;
    }
}