package pt.ulusofona.lp2.crazyChess;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

public class Simulador {
    int dimensao;
    int nrPecas;
    HashMap<Integer,CrazyPiece> hm = new HashMap<Integer, CrazyPiece>();
    Jogador[] jogadores = new Jogador[]{new Jogador(0),
            new Jogador(1)};
    int[][] tabuleiro;
    // false=pretas && true=brancas
    boolean turn = true;
    int nrTurn = 0;

    public Simulador(){

    }
    /*DUVIDA
        ->Juntar a matriz tabuleiro com o HM
        ->Hm continua com todas as peças iniciais, mesmo as capturadas
    */

    //Funçao de leitura de ficheiro
    public boolean iniciaJogo(File ficheiro){
        try {

            Scanner leitorFicheiro = new Scanner(ficheiro);
            int lineNumber=0;
            while(leitorFicheiro.hasNextLine()) {
                lineNumber++;
                String linha = leitorFicheiro.nextLine();
                String dados[] = linha.split(":");

                //Linha = 1 => dimensao
                if(lineNumber == 1 ){
                    int aux = Integer.parseInt(dados[0]);
                    //Verificar se a dimensao é valida
                    if((aux >=4 && aux<=12)){
                        dimensao = aux;
                        tabuleiro = new int[dimensao][dimensao];
                    }else {return false;}

                    //Linha = 2 => nrPeças
                }else if(lineNumber == 2){
                    int aux = Integer.parseInt(dados[0]);
                    //Verificar se nr e Peças é valido
                    if(aux < (dimensao * dimensao)){
                        this.nrPecas = aux;
                    }else {return false;}
                    // Criar peça e adicionar ao hashmap
                }else if(lineNumber > 2 && lineNumber <= nrPecas+2){//2<Linha<=nrPeça+1 => ler objectos e guardar num hashmap
                    int id = Integer.parseInt(dados[0]);
                    int tipo = Integer.parseInt(dados[1]);
                    int idEquipa = Integer.parseInt(dados[2]);
                    String alcunha = dados[3];

                    CrazyPiece c = new CrazyPiece(id,tipo,idEquipa,alcunha);
                    //verificar se já existe, apenas adicionar se nao existir
                    if(hm.containsKey(id) == false){
                        hm.put(id,c);
                    }
                    //Verifica a que jogador deve atribuir a peça
                    if(idEquipa == 0){
                        jogadores[0].addPeca(c);
                    }else{
                        jogadores[1].addPeca(c);
                    }
                }else{

                    //(nrPeca+3) porque nao queremos as linhas de dimensao nem as das peças
                    //e o +3 são as duas primeiras linhas +1 para contabilizar o 0
                    int y = lineNumber-(nrPecas+3);
                    //vamos percorrer a linha lida e verificar se temos uma posição a 0 ou nao
                    for(int i = 0;i < dimensao; i++){
                        int aux = Integer.parseInt(dados[i]);
                        //se nao
                        if(aux != 0){
                            boolean verificaPeçaExiste = hm.containsKey(aux);
                            //verificar se o id da peça encontrada existe no hashmap
                            if (verificaPeçaExiste == true) {
                                //ir buscar a peça com o id que encontramos
                                CrazyPiece piece = hm.get(aux);
                                //atualizar as coordenadas
                                piece.setPieceCoord(i, y);
                                tabuleiro[i][y] = aux;
                            }
                        }else{tabuleiro[i][y] = 0;}
                    }
                }
            }
            leitorFicheiro.close();
        }
        catch(FileNotFoundException exception) {
            String nomeFicheiro = ficheiro.getName(); // Vou buscar o nome do ficheiro, pois a definição da função apenas nos deixa passar um FILE
            String mensagem = "Erro: o ficheiro " + nomeFicheiro + " nao foi encontrado.";
            System.out.println(mensagem);
            return false;
        }
        return true;
    }
    public int getTamanhoTabuleiro(){ ;
        return this.dimensao;
    }
    public boolean processaJogada(int xO, int yO, int xD, int Yd){

        int idPecaJogada = tabuleiro[xO][yO];

        //Vou buscar a peca a ser jogada
        CrazyPiece pecaJogada = hm.get(idPecaJogada);
        int equipa = pecaJogada.getIdEquipa();
        //turn = false -> equipa branca a jogar
        //turn = true-> equipa preta a jogar
        if((equipa==1 && turn == false) || (equipa==0 && turn == true)){
            jogadores[equipa].incrementaTentativasInvalidas();
            return false;
        }

        //Verifica se as posiçoes sao validas
        if(xD > dimensao || Yd > dimensao || Yd < 0 || xD < 0){
            jogadores[equipa].incrementaTentativasInvalidas();
            return false;
        }if(xO > dimensao || yO > dimensao || yO < 0 || xO < 0){
            jogadores[equipa].incrementaTentativasInvalidas();
            return false;
        }
        //Verifica se avança uma unidade
        if (xD == xO + 1 || Yd == yO + 1 || xD == xO - 1|| Yd == yO - 1) {
            //Verifica se a posiçao esta ocupada
            if(tabuleiro[xD][Yd] == 0) {
                tabuleiro[xD][Yd] = idPecaJogada;
                pecaJogada.setPieceCoord(xD, Yd);
                //Apagar posicao anterior
                tabuleiro[xO][yO] = 0;
                //Troca de turno
                turn = !turn;

                jogadores[equipa].incrementaJogadasValidas();
                nrTurn++;
                return true;
            }//verificar se existe peca na posicao destino
            else if(hm.containsKey(tabuleiro[xD][Yd])){
                CrazyPiece c = hm.get(tabuleiro[xD][Yd]);
                //Id Diferente captura o C
                if(c.getIdEquipa()!=pecaJogada.getIdEquipa()){
                    jogadores[c.getIdEquipa()].removePeca(c);
                    tabuleiro[xD][Yd] = idPecaJogada;
                    pecaJogada.setPieceCoord(xD, Yd);
                    //Apagar posicao anterior
                    tabuleiro[xO][yO] = 0;
                    //Troca de turno
                    turn = !turn;

                    jogadores[equipa].incrementaJogadasValidas();
                    jogadores[equipa].incrementaCapuradas();
                    nrTurn=0;
                    return true;
                    // Peças de da mesma equipa
                }else{
                    jogadores[equipa].incrementaJogadasValidas();
                    return false;
                }
            }
        }
        jogadores[equipa].incrementaTentativasInvalidas();
        return false;
    }
    public int getIDPeca(int x, int y){
        return tabuleiro[x][y];
    }
    public List<CrazyPiece> getPecasMalucas(){
        List<CrazyPiece> list = new ArrayList<CrazyPiece>(hm.values());
        return list;
    }
    public List<String> getAutores(){
        List<String> autores = new ArrayList<String>();
        autores.add("Marcelo Costa || 21705266 || LEIRT");
        autores.add("Bijel Premgi || 21703957 || LEI");
        return autores;
    }
    public  int getIDEquipaAJogar(){
        if(turn){return 1;}
        else{return 0;}
    }
    public List<String> getResultados(){
        List<String> resultados = new ArrayList<String>();
        resultados.add("JOGO DE CRAZY CHESS");
        if(jogadores[0].listaComprimento() == 0 && jogadores[1].listaComprimento() > 0){
            resultados.add("Resultado: VENCERAM AS PRETAS");
        }
        else if(jogadores[1].listaComprimento() == 0 && jogadores[0].listaComprimento() > 0){
            resultados.add("Resultado: VENCERAM AS BRANCAS");
        }
        else{
            resultados.add("Resultado: EMPATE");
        }
        resultados.add("---");
        resultados.add("Equipa das Pretas");
        //vamos buscar um array com as estatisticas da equipa das pretas
        //Posicoes : 0->Capturadas || 1->jogadas Validas || 2->jogadas Invalidas
        List<Integer> estatisticasPretas = jogadores[1].getEstatisticas();
        resultados.add(String.valueOf(estatisticasPretas.get(0)));
        resultados.add(String.valueOf(estatisticasPretas.get(1)));
        resultados.add(String.valueOf(estatisticasPretas.get(2)));

        resultados.add("Equipa das Brancas");
        List<Integer> estatisticas = jogadores[0].getEstatisticas();
        resultados.add(String.valueOf(estatisticas.get(0)));
        resultados.add(String.valueOf(estatisticas.get(1)));
        resultados.add(String.valueOf(estatisticas.get(2)));

        return resultados;
    }
    public boolean jogoTerminado(){
        if(jogadores[0].listaComprimento() == 0 || jogadores[1].listaComprimento() == 0){
            return true;
        }else if(jogadores[0].listaComprimento() == 1 || jogadores[1].listaComprimento() == 1){
            return true;
        }else if(nrTurn >= 10){
            return true;
        }
        return false;
    }
}

//FAZER
//Não existe uma peça (da cor da equipa activa) nas coordenadas de origem;
//vERIFICAR SE NA PEÇA DE ORIGEM EXISTE UMA PEÇA DA EQUIPA A JOGAR -> JOGADA INVALIDA
//As coordenadas de destino estão ocupadas por uma peça da mesma cor.
//Testes
