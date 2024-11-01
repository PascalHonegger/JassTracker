<script lang="ts" setup>
import { computed, ref } from "vue";

const props = defineProps<{ max: number; modelValue: number | null }>();
const emit = defineEmits<{
  (event: "update:modelValue", value?: number): void;
}>();

const input = ref<HTMLInputElement>();
const enteredScore = ref(props.modelValue?.toString() ?? "");
const score = computed(() =>
  enteredScore.value === "" ? undefined : parseInt(enteredScore.value, 10),
);
const inRange = computed(() => Math.abs(score.value ?? 0) <= props.max);

function handleKeypress(event: KeyboardEvent) {
  const { key } = event;
  if (key === "Enter") {
    input.value?.blur();
  }
  if (!/^[-\d]$/.test(key)) {
    event.preventDefault();
  }
}

function handleChange() {
  if (inRange.value) {
    let { value } = score;
    if (value !== undefined && (value < 0 || Object.is(value, -0))) {
      value = props.max + value;
      enteredScore.value = value.toString();
    }
    emit("update:modelValue", value);
  }
}
</script>

<template>
  <input
    ref="input"
    v-model="enteredScore"
    inputmode="numeric"
    :class="{ '!bg-red-100': !inRange }"
    :min="-max"
    :max="max"
    @change="handleChange"
    @keypress="handleKeypress"
  />
</template>
