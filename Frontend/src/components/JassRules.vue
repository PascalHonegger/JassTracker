<script setup lang="ts">
import HelpTabs from "@/components/HelpTabs.vue";
</script>
<style lang="postcss" scoped>
h3 {
  @apply text-lg font-normal leading-normal mt-4;
}
</style>
<template>
  <div class="container mx-auto text-center">
    <HelpTabs class="flex border-b border-black mt-4">
      <RouterLink
        class="text-blue-600 inline-block py-2 px-4 hover:text-blue-800 font-semibold"
        to="/help/jassTrackerHelp"
        >JassTracker Bedienung</RouterLink
      >
      <div
        class="-mb-px mr-1 border-black inline-block bg-white inline-block border-l border-t border-r rounded-t py-2 px-4 font-semibold"
      >
        Jass Regeln
      </div>
    </HelpTabs>
    <h1 class="text-5xl font-normal leading-normal mt-4">Jass Regeln</h1>
    <h2 class="text-4xl font-normal leading-normal mt-4">Spielablauf</h2>
    <div class="flex py-2">
      <div class="text-left flex-wrap">
        <img
          class="float-right object-scale-down max-w-[40%] pl-4 pb-4"
          src="../assets/helpcenter/gameplay.webp"
          alt="Spielablauf"
        />
        <p>
          Der Coiffeur ist ein Partnerjass, bei dem jeweils die beiden gegenüberliegenden Spieler
          ein Team bilden.
          <br />
          Es geht darum, sämtliche Trumpfarten einmal zu spielen und dabei möglichst viele Punkte zu
          erzielen. Es werden jeweils nur die Punkte des ansagenden Teams geschrieben.
          <br />
          Jedem Trumpf ist ein Multiplikator zugeteilt, mit welchem am Ende der Runde die erzielten
          Punkte multipliziert werden. Dieser Umstand erfordert eine vorausschauende Planung bei der
          Trumpfwahl.
          <br />
          Beim Coiffeur auf 8 Runden müssen von jedem Team jede Trumpffarbe, Obenabe, Undenufe, und
          zwei Joker (freie Wahl der Trumpfart) je einmal gespielt werden. Beim Coiffeur auf 10
          Runden müssen zusätzlich einmal Slalom und einmal Guschti 5/4 gespielt werden.
        </p>
        <h3>Slalom</h3>
        <p>
          Beim Slalom wird abwechslungsweise ein Stich Obenabe und dann ein Stich Undenufe gespielt.
          <br />
          Beim Slalom-Obenabe wird der erste Stich Obenabe gespielt. Beim Slalom-Undenufe wird
          hingegen mit Undenufe begonnen.
          <br />
        </p>
        <h3>Guschti</h3>
        <p>
          Beim Guschti-Obenabe werden zuerst fünf Stiche Obenabe und dann vier Stiche Undenufe.
          <br />
          Beim Guschti-Undenufe zuerst fünf Stiche Undenufe und dann vier Stiche Obenabe gejasst.
          <br />
        </p>
        <h3>Schieben</h3>
        <p>
          Das Schieben erfolgt nicht wie beim Schieber zum Partner, sondern im Gegenuhrzeigersinn
          zum nächsten Mitspieler.
          <br />
          Schieben alle Spieler weiter, kommt die Aufforderung zum Trumpfen wieder zum ersten
          Spieler zurück. Dieser kann nicht mehr weiterschieben und muss einen Trumpf wählen.
          Derjenige Spieler, welcher die Trumpfart bestimmt hat, startet mit dem Ablegen der ersten
          Karte. Der Coiffeur wird ohne Weisen gespielt.
        </p>
        <h3>Wer Gewinnt?</h3>
        <p>Das Team, welches nach Beendigung aller Runden am meisten Punkte aufweist, gewinnt.</p>
      </div>
    </div>

    <h2 class="text-4xl font-normal leading-normal mt-4">Spielregeln</h2>
    <div class="flex py-2">
      <div class="text-left flex-wrap">
        <img
          class="float-left object-scale-down max-w-[40%] pr-4 pb-4"
          src="../assets/helpcenter/rules.webp"
          alt="Regeln"
        />
        <p class="text-left">
          Beim Coiffeur gibt es klare Regeln, wann welche Farben gespielt werden dürfen.
        </p>
        <h3>Regel 1</h3>
        <p>
          Wer eine Karte der ausgespielten Farbe besitzt, muss diese Farbe spielen. Die Ausnahme ist
          der Trumpf-Bauer/Trumpf-Under.
        </p>
        <h3>Regel 2</h3>
        <p>
          Wer keine Karte der ausgespielten Farbe hat, darf irgendeine Karte spielen (Untertrumpfen
          ist nur erlaubt, wenn man nur noch Trumpfkarten besitzt).
        </p>
        <h3>Regel 3</h3>
        <p>Es darf jederzeit ein Trumpf gespielt werden, anstatt Farbe zu bekennen.</p>
        <h3>Regel 4</h3>
        <p>
          Wurde die Trumpffarbe ausgespielt, dann muss ebenfalls eine Trumpfkarte gespielt werden.
          Wer neben dem Trumpf-Bauer/Trumpf-Under keine andere Trumpfkarte mehr besitzt, darf statt
          des Trumpf-Bauers/Trumpf-Unders auch jede andere Karte spielen.
        </p>
      </div>
    </div>
  </div>
</template>
