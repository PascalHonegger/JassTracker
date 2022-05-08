with mapping (old_name, new_name) as (
    values
        ('Eicheln', 'Eichle'),
        ('Rosen', 'Rose'),
        ('Schilten', 'Schilte'),
        ('Schellen', 'Schälle')
)
update contract
    set name = (select new_name from mapping where name = old_name)
    where name in (select old_name from mapping);
