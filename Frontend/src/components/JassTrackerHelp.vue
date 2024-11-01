<script setup lang="ts">
import HelpTabs from "@/components/HelpTabs.vue";
</script>

<template>
  <div class="container mx-auto text-center">
    <HelpTabs class="flex border-b border-black mt-4">
      <div
        class="-mb-px mr-1 border-black inline-block bg-white inline-block border-l border-t border-r rounded-t py-2 px-4 font-semibold"
      >
        JassTracker Bedienung
      </div>
      <RouterLink
        class="inline-block text-blue-600 py-2 px-4 hover:text-blue-800 font-semibold"
        to="/help/jassRules"
        >Jass Regeln</RouterLink
      >
    </HelpTabs>
    <h1 class="text-5xl font-normal leading-normal mt-4">JassTracker Bedienung</h1>
    <h2>Login / Registrierung</h2>
    <div class="flex py-2">
      <div class="text-left flex-wrap">
        <img
          class="float-right object-scale-down max-w-[40%] pl-4"
          src="../assets/helpcenter/login.webp"
          alt="Login Screen"
        />
        <p>
          Der JassTracker kann entweder als Gast oder mit einem erstellen Benutzerprofil benutzt
          werden.
          <br />
          Um einen Benutzeraccount zu erhalten, muss man sich zuerst registrieren unter
          <RouterLink to="/register" class="underline text-blue-600">Registrieren</RouterLink>
          . Danach kann man sich mit den Benutzerinformationen einloggen und den JassTracker im
          vollen Umfang verwenden.
          <br />
          Wenn man sich keinen Account erstellen möchte, oder den JassTracker mal ausprobieren
          möchte, kann man sich als "Gast" anmelden. Man kann alle Funktionalitäten der Applikation
          ausprobieren und sehen, ob man einen Account erstellen möchte.
        </p>
      </div>
    </div>
    <h2>Tische</h2>
    <div class="flex py-2">
      <div class="text-left flex-wrap">
        <img
          class="float-left object-scale-down max-w-[30%] pr-4"
          src="../assets/helpcenter/tables.webp"
          alt="Tisch Übersicht"
        />
        <p>
          Sobald man sich eingeloggt hat (entweder mit dem eigenen Benutzer Account oder als Gast),
          kann man neue Tische erstellen.
          <br />
          Tische enthalten immer 4 Personen, somit hat man alle Spiele die man mit diesen Personen
          spielt, immer an einem Ort und sorgt für eine bessere Übersicht.
          <br />
          Ein Tisch kann zudem einen Namen haben, z.B. "Familientisch", wo man immer mit seinen
          Familienmitgliedern spielt.
        </p>
      </div>
    </div>
    <div class="flex py-2">
      <div class="text-left flex-wrap">
        <img
          class="float-right object-scale-down max-w-[50%] pl-4"
          src="../assets/helpcenter/tableCreation.webp"
          alt="Tisch Erstellen"
        />
        <p>
          Beim Erstellen eines Tisches kann man via Drag-and-Drop Spieler auf die einzelnen Plätze
          ziehen. Dabei ist angedacht, dass das Feld mit ihrem Anzeigenamen an ihren Platz am Tisch
          gezogen wird und die restlichen Plätze mit dem "Neuer Spieler"-Feld um einen Alias ergänzt
          werden. Später wenn man einen neues Spiel erstellt (wie im Screenshot angezeigt), kann man
          eine Platzänderung via Drag-and-Drop vornehmen, oder via dem "Neuer Spieler"-Feld einen
          neuen Teilnehmer am Tisch hinterlegen.
        </p>
      </div>
    </div>
    <h2>Spiele</h2>
    <div class="flex py-2">
      <div class="text-left flex-wrap">
        <img
          class="float-left object-scale-down max-w-[40%] pr-4 pb-4"
          src="../assets/helpcenter/scoreboard.webp"
          alt="Spiel Übersicht"
        />
        <p>
          Spiele sind immer in einem Tisch enthalten. Bei der Erstellung eines Tisches wird
          automatisch das erste Spiel erstellt.
          <br />
          Spiele können mit "Spiel abschliessen", entweder frühzeitig oder sobald alle Möglichkeiten
          gespielt wurden, abgeschlossen werden.
          <br />
          Ein neues Spiel kann via "Neues Spiel erstellen" eröffnet werden. Auf Wunsch kann man die
          Teams neu bilden. erhält die Option, die Teams neu zu bilden bei Wunsch.
          <br />
          Beim Klick auf ein Spiel in der unteren Box Ansicht, kommt man auf die Detailseite, wo man
          die Möglichkeit hat, ein Spiel zu löschen. Nur abgeschlossene Spiele können gelöscht
          werden.
          <br />
          In dieser Detailansicht, sieht man zusätzlich aktuelle Spielstatistiken wie aktuelle
          Siegeschancen pro Team oder Durchschnittliche Punktzahl etc.
          <br />
          Anstatt z.B. 157 - 15 zu rechnen und dann 142 einzugeben, kann auch direkt "-15"
          eingetragen werden. Eingaben im Spiel können angepasst und auch gelöscht werden. Es werden
          nur Zahlen und "-" in den Feldern erlaubt. Bei einer invaliden Eingabe wird das Feld rot
          markiert, bis der Fehler korrigiert wurde.
        </p>
      </div>
    </div>
    <h2>Profil / Statistiken</h2>
    <div class="flex py-2">
      <div class="text-left flex-wrap">
        <img
          class="float-right object-scale-down max-w-[50%] pl-4 pb-4"
          src="../assets/helpcenter/statistics.webp"
          alt="Statistiken"
        />
        <p>
          Unter dem
          <RouterLink to="/profile" class="underline text-blue-600">Profil</RouterLink>
          kann man seine Benutzerdaten anpassen und sieht auch persönliche Statistiken wie die
          durchschnittliche Punktzahl pro Trumpf und die Häufigkeit von Punktzahlen.
          <br />
          Der "Benutzername" lässt sich nicht anpassen, nachdem er initial gesetzt wurde.
          <br />
          Die Statistiken zeigen auf einem Tisch generelle Statistiken an wie z.B. Punktezahl pro
          Spiel und Team, die durchschnittliche Punktzahl pro Spieler und die durchschnittliche
          Punktezahl pro Trumpf.
          <br />
          Mit einem klick auf die Detaillierte Auswertung, sieht man Statistiken zum ausgewählten
          Spiel. Punktzahlen pro Runden, der Punkteunterschied zwischen Team 1 und Team 2
          (dargestellt im Bild Rechts) und die durchschnittlichen Punktzahlen.
        </p>
      </div>
    </div>
    <p class="py-8">
      Jasskarten-Icons bereitgestellt von Wikimedia Commons User Jensche, lizensiert unter
      <a target="_blank" class="underline" href="https://creativecommons.org/licenses/by-sa/4.0/"
        >Creative Commons Attribution-Share Alike 4.0 International Lizenz</a
      >.
    </p>
    <p>
      Esstisch wurde erstellt von
      <a target="_blank" class="underline" href="https://www.freepik.com/vectors/dinner-table"
        >www.freepik.com</a
      >.
    </p>
  </div>
</template>
<style lang="postcss" scoped>
h2 {
  @apply text-4xl font-normal leading-normal mt-4;
}
</style>
