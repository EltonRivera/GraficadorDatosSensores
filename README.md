GraficadorDatosSensores
=======================

Interfaz JAVA de un Arduino, que graficará Temperatura vs Tiempo y mostrará un medidor indicando la temperatura.

Instalación de librerías RXTX en Sistemas Windows x64 y Linux x64
===============

Windows
----------------------------------------------------

Copiar RXTXcomm.jar ---> <JAVA_HOME>\jre\lib\ext

Copiar rxtxSerial.dll ---> <JAVA_HOME>\jre\bin

Copiar rxtxParallel.dll ---> <JAVA_HOME>\jre\bin



Linux
----------------------------------------------------

Copiar RXTXcomm.jar ---> <JAVA_HOME>/jre/lib/ext

Copiar librxtxSerial.so ---> <JAVA_HOME>/jre/lib/x86_64/

Copiar librxtxParallel.so ---> <JAVA_HOME>/jre/lib/x86_64/

NOTA: Para un JDK con arquitéctura=i386, solo cambiar
x86_64 a i386 en la parte de arriba.

Esas librerías se encuentra dentro del proyecto en la carpeta "/lib".

Créditos por la librería RXTX:
----

RXTX binary builds provided as a courtesy of Mfizz Inc. (http://mfizz.com/).
Please see http://mfizz.com/oss/rxtx-for-java for more information.
