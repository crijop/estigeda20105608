package eda;

import java.awt.RenderingHints;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

public class TesteImagem {

public static void main(String[] args) throws IOException {
/*Carrega a imagem, não utiliza Java.io.*,
isso é bom, pois não teremos que trabalhar com bytes,
a api cuida disso.*/
PlanarImage pi = JAI.create("fileload", "images/carlos.jpg");

System.out.println("Largura = " + pi.getWidth());
System.out.println("Altura = " + pi.getHeight());

/*Calculo para o novo tamanho, pois o JAI trabalha com
ponto flutuante como valor para o novo size.*/
double newSize = ((double) 300) / ((double) pi.getWidth());

/*Aqui são passadas a imagem que queremos aplicar
o resize e o novo tamanho para ela.*/
ParameterBlock pb = new ParameterBlock();
pb.addSource(pi);
pb.add(newSize);
pb.add(newSize);

/*É selecionado o tipo de renderização, no caso, esta fará a
interpolação da imagem. Que no caso fará um resize com um
ótimo efeito, como se tivesse feito num editor de
imagens(Gimp ou Photoshop).*/
RenderingHints qualityHints = new RenderingHints(
RenderingHints.KEY_INTERPOLATION,
RenderingHints.VALUE_RENDER_QUALITY);

//É aplicado o efeito.
pi = JAI.create("SubsampleAverage", pb, qualityHints);

/*Como já foi utilizado o ParameterBlock,
tem que apontar para um novo objeto*/
pb = new ParameterBlock();

//É informado o local e o nome da nova imagem e o formato.
pb.addSource(pi);
pb.add("images/resized.jpg");
pb.add("jpeg");
JAI.create("filestore", pb);

//Cria a imagem em disco redimencionada.
pi = JAI.create("fileload", "images/resized.jpg");

System.out.println("Largura = " + pi.getWidth());
System.out.println("Altura = " + pi.getHeight());

}

}

