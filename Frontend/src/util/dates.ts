const dateTimeFormat = new Intl.DateTimeFormat("de-CH", {
  dateStyle: "medium",
  timeStyle: "short",
});

export function toDateTimeString(date: Date) {
  return dateTimeFormat.format(date);
}
