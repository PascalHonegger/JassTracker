const dateTimeFormat = new Intl.DateTimeFormat("de-CH", {
  dateStyle: "medium",
  timeStyle: "medium",
});

export function toDateTimeString(date: Date) {
  return dateTimeFormat.format(date);
}
