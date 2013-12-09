;; Universidad de Guadalajara - CUCEI
;; Sistemas Expertos 2013B
;; Miramontes, Orlando O
;; Reglas que modelan una respuesta generica en un juego de ajedrez 

(clear)

(defrule gana
    "Se puede tomar el rey"
    (posicion-oponente rey ?pos-rey-oponente)
    (ataca si ?pieza a ?pos)
    =>
    (assert (mueve ?pieza a ?pos)))
    
(defrule intimida
    "Hacer un movimiento para provocar el oponente a quitar una pieza de cierto lugar"
    (posicion-oponente ?victima ?pos)
    (mover ?pieza a ?pos-ataque)
    (ataca si ?pieza a ?pos desde ?pos-ataque)
    =>
    (assert (mueve ?pieza a ?pos-ataque)))

(defrule intimida-protegida
    "Jaque/Piesas regulares"
    (posicion-oponente ?victima ?pos)
    (mover ?pieza a ?pos-ataque)
    (ataca si ?pieza a ?pos desde ?pos-ataque)
    (protegida ?pos-ataque)
    =>
    (assert (mueve ?pieza a ?pos-ataque)))
    
(defrule aleatorio-no-atacada
    "Ir a una posicion no atacada"
    (oponente ?oponente)
    (mover ?pieza a ?pos-bien)
    (ataca no ?oponente a ?pos-bien)
    =>
    (assert (mueve ?pieza a ?pos-bien)))

(defrule aleatorio-protegido
    "Ir a una posicion protegida"
    (mover ?pieza a ?pos)
    (protegida ?pos)
    =>
    (assert (mueve ?pieza a ?pos)))

(defrule protege
    "Mover una pieza a una posicion no atacada para proteger otra"
    (mover ?pieza a ?pos-p)
    (posicion ?aliado ?pos-a)
    (diferente ?pieza ?aleado)
    (ataca si ?pieza a ?pos-a desde ?pos-p)
    (oponente ?oponente)
    (ataca no ?oponente a ?pos-p)
    =>
    (assert (mueve ?pieza a ?pos-p)))
    
(defrule provoca
    "Ir a lugar atacado para provocar perdida de piezas en ambos equipos"
    (oponente ?oponente)
    (mover ?pieza a ?pos-ataque)
    (ataca si ?oponente a ?pos-ataque)
    (menor-igual-valor ?pieza ?oponente)
    (protegida ?pos-ataque)
    =>
    (assert (mueve ?pieza a ?pos-ataque)))

(defrule tienes-pieza-atacada
    "Pregunta inicial de defenza"
    (posicion-oponente ?atacante ?pos-op)
    (posicion ?pieza ?pos)
    (ataca si ?atacante ?pos)
    =>
    (assert (protege a ?pieza ?pos de ?atacante ?pos-op)))

(defrule proteger-pieza-toma-protegido
    "Tomar una pieza atacante, y asegurarse que la pieza que se mueve esta protegida"
    (protege a ?victima ?pos-vi de ?atacante ?pos-at)
    (mover ?pieza a ?pos-at)
    (protegida ?pos-at)
    =>
    (assert (mueve ?pieza a ?pos-at)))
    
(defrule proteger-pieza-toma
    "Tomar una pieza atacante"
    (protege a ?victima ?pos-vi de ?atacante ?pos-at)
    (mover ?pieza a ?pos-at)
    =>
    (assert (mueve ?pieza a ?pos-at)))

(defrule proteger-pieza-protegido
    "Proteger la pieza atacada con una pieza que quede protegida despues del movimiento"
    (protege a ?victima ?pos-vi de ?atacante ?pos-at)
    (obstruye ?pieza ?pos ?atacante)
    (protegida ?pos)
    =>
    (assert (mueve ?pieza a ?pos)))
    
(defrule proteger-pieza
    "Proteger la pieza atacada con una pieza"
    (protege a ?victima ?pos-vi de ?atacante ?pos-at)
    (obstruye ?pieza ?pos ?atacante ?pos-at)
    =>
    (assert (mueve ?pieza a ?pos)))

(defrule proteger-pieza-retirar
    "Retirar una pieza"
    (protege a ?victima ?pos-vi de ?atacante ?pos-at)
    (mover ?victima ?pos)
    (oponente ?oponente)
    (ataca no ?oponente ?pos)
    =>
    (assert (mueve ?victima a ?pos)))
    
(defrule tomar-pieza
    "Tomar una pieza"
    (posicion-oponente ?oponente ?posicion)
    (mover ?pieza ?posicion)
    =>
    (assert (mueve ?pieza a ?posicion)))
    
(defrule tomar-pieza-protegido
    "Tomar una pieza"
    (posicion-oponente ?oponente ?posicion)
    (mover ?pieza ?posicion)
    (protegida ?posicion)
    =>
    (assert (mueve ?pieza a ?posicion)))
    
(defrule impirmir-mueve
    "Imprimir la orden de tomar una pieza"
    (mueve ?pieza a ?posicion)
    =>
    (printout t "Mueve " ?pieza " a " ?posicion crlf))

(defrule imprime-protegida
    (mueve ?pieza a ?posicion)
    (protegida ?posicion)
    =>
    (printout t "Mover " ?pieza " a " ?posicion " sera una buena desicion" crlf))
    
(defrule imprime-libre
    (mueve ?pieza a ?posicion)
    (ataca no ? ?posicion)
    =>
    (printout t "Mover " ?pieza " a " ?posicion " no te causara perdida de piezas" crlf))
    
(defrule imprime-obstruye
    (obstruye ?pieza ?pos ?atacante ?pos-at)
    =>
    (printout t "Para obstruir el camino a " ?atacante " evalua si " ?pieza " es de valor menor a la pieza que protegeras"))

(defrule impirmir-protege
    "Imprimir la orden de tomar una pieza"
    (protege a ?victima ?pos-vi de ?atacante ?pos-at)
    =>
    (printout t "Protege a " ?victima " " ?pos-vi " de " ?atacante " " ?pos-at crlf))
