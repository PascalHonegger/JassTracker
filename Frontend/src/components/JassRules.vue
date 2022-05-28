<template>
  <div class="container mx-auto text-center">
    <h1 class="text-5xl font-normal leading-normal mt-4">Jass Regeln</h1>
    <h2 class="text-4xl font-normal leading-normal mt-4">Spielablauf</h2>
    <div class="flex py-2">
      <p class="text-left">
        Der Coiffeur ist ein Partnerjass, bei dem jeweils die beiden
        gegenüberliegenden Spieler ein Team bilden.
        <br />
        Beim Coiffeur geht es darum, sämtliche Trumpfarten einmal zuspielen und
        dabei möglichst viele Punkte zu erzielen. Es werden jeweils nur die
        Punkte des ansagenden Teams geschrieben.
        <br />
        Das Team, welches nach Beendigung aller Runden am meisten Punkte
        aufweist, gewinnt.
        <br />
        Jedem Trumpf ist ein Multiplikator zugeteilt, mit welchem am Ende der
        Runde die erzielten Punkte multipliziert werden. Dieser Umstand
        erfordert eine vorausschauende Planung bei der Trumpfwahl.
        <br />
        Beim Coiffeur auf 8 Runden müssen von jedem Team jede Trumpffarbe,
        Obenabe, Undenufe, und zwei Joker (freie Wahl der Trumpfart) je einmal
        gespielt werden. Beim Coiffeur auf 10 Runden müssen zusätzlich einmal
        Slalom und einmal Guschti 5/4 gespielt werden.
        <br />
        Beim Slalom wird abwechslungsweise ein Stich Obenabe und dann ein Stich
        Undenufe gespielt.
        <br />
        Beim Slalom-Obenabe wird der erste Stich Obenabe gespielt. Beim
        Slalom-Undenufe wird hingegen mit Undenufe begonnen.
        <br />
        Beim Guschti-Obenabe werden zuerst fünf Stiche Obenabe und dann vier
        Stiche Undenufe
        <br />
        Beim Guschti-Undenufe zuerst fünf Stiche Undenufe und dann vier Stiche
        Obenabe gejasst.
        <br />
        Das Schieben erfolgt nicht wie beim Schieber zum Partner, sondern im
        Gegenuhrzeigersinn zum nächsten Mitspieler.
        <br />
        Schieben alle Spieler weiter, kommt die Aufforderung zum Trumpfen wieder
        zum ersten Spieler zurück. Dieser kann nicht mehr weiterschieben und
        muss einen Trumpf wählen. Derjenige Spieler, welcher die Trumpfart
        bestimmt hat, startet mit dem Ablegen der ersten Karte. Der Coiffeur
        wird ohne Weisen gespielt.
      </p>
      <img
        class="max-w-[50%] h-full pl-4"
        src="../assets/helpcenter/gameplay.webp"
        alt="Spielablauf"
      />
    </div>

    <h2 class="text-4xl font-normal leading-normal mt-4">Spielregeln</h2>
    <div class="flex py-2">
      <img
        class="max-w-[50%] pr-4"
        src="../assets/helpcenter/rules.webp"
        alt="Regeln"
      />
      <p class="text-left">
        Beim Coiffeur gibt es klare Regeln, wann welche Farben gespielt werden
        dürfen.
        <br />
        1. Wer eine Karte der ausgespielten Farbe besitzt, muss diese Farbe
        spielen. Die Ausnahme ist der Trumpf-Bauer/Trumpf-Under.
        <br />
        2. Wer keine Karte der ausgespielten Farbe hat, darf irgendeine Karte
        spielen (Untertrumpfen ist nur erlaubt, wenn man nur noch Trumpfkarten
        besitzen).
        <br />
        3. Es darf jederzeit einen Trumpf gespielt werden anstatt Farbe zu
        bekennen.
        <br />
        4. Wurde die Trumpffarbe ausgespielt, dann muss ebenfalls eine
        Trumpfkarte gespielt werden. Wer neben dem Trumpf-Bauer/Trumpf-Under
        keine andere Trumpfkarte mehr besitzt, darf statt des
        Trumpf-Bauer/Trumpf-Under auch jede andere Karte spielen.
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: "JassRules",
};
</script>
