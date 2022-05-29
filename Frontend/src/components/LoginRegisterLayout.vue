<template>
  <div class="grid grid-rows-1 grid-cols-1 md:grid-cols-2 w-full h-full">
    <div class="image hidden md:block h-full">
      <img
        loading="lazy"
        class="object-cover h-full w-full"
        src="../assets/jass.webp"
        alt="Jasskarten"
      />
    </div>
    <div class="login text-center flex flex-col h-full overflow-auto">
      <h1 class="font-medium leading-tight text-5xl mb-2 text-blue-600 mt-auto">
        JassTracker
      </h1>
      <p class="font-medium">Coiffeur Jass auf das nächste Level anheben</p>
      <slot />
    </div>
  </div>
</template>
