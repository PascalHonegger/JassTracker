<template>
  <div class="modal-backdrop">
    <div class="modal">
      <header class="modal-header">
        <slot name="header"></slot>
        <button type="button" class="btn-close" @click="close">x</button>
      </header>

      <section class="modal-body">
        <slot name="body"></slot>
      </section>

      <footer class="modal-footer">
        <slot name="footer"></slot>
      </footer>
    </div>
  </div>
</template>

<script lang="ts">
export default {
  name: "ModalComponent",
  methods: {
    close() {
      this.$emit("close");
    },
  },
};
</script>
