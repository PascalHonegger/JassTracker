const dateTimeFormat = new Intl.DateTimeFormat("de-CH", {
  dateStyle: "medium",
  timeStyle: "short",
});

export function toDateTimeString(date: Date) {
  return dateTimeFormat.format(date);
}

export function dateCompare(a: Date, b: Date): number {
  return a.getTime() - b.getTime();
}
