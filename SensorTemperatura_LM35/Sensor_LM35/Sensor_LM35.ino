/* 
 MODO DE CONEXIÓN DEL SENSOR
 
 Conectamos el pin 1 que corresponde a la alimentación del sensor con los 5V del Arduino
 Conectamos el pin 2 que corresponde al pin de datos del sensor con cualquier pin analógico del Arduino A0
 Conectamos el pin 3 que corresponde al pin de masa (GND) del sensor con el pin GND del Arduino
 
*/
 
int temp = 0;  //Pin analógico A0 del Arduino donde conectaremos el pin de datos del sensor LM35
float maxC = 0, minC = 100;  //Variables para ir comprobando maximos y minimos
 
void setup()
{
  Serial.begin(9600);  //Iniciamos comunicación serie con el Arduino para ver los resultados del sensor
                       //a través de la consola serie del IDE de Arduino
}
 
void loop()
{
  float gradosC;  //Declaramos estas variables tipo float para guardar los valores de lectura
                                                           
  gradosC = (5.0 * analogRead(temp) * 100.0) / 1024;  //Esta es la funcion con la que obtenemos la medida del sensor
                                                      // en ºC
                                            
                                                                                                    
  //Mostramos mensaje con valores actuales de temperatura, asi como maximos y minimos de cada uno de ellos
  Serial.print("Medidas actuales\n");
  Serial.print("C: "); 
  Serial.print(gradosC);
  //Comprobacion de maximos y minimos de temperatura
  if (maxC < gradosC)
    maxC = gradosC;
  if (gradosC < minC)
    minC = gradosC;
  Serial.print("\nMedidas maximas\n");
  Serial.print("C: "); 
  Serial.print(maxC);
  Serial.print("\nMedidas minimas\n");
  Serial.print("C: "); 
  Serial.print(minC);
  Serial.print("\n\n");
  delay(2000);  //Usamos un retardo de 2 segundos entre lecturas
}
