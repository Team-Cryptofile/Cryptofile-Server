
create table users
(
    idusers int auto_increment,
    username varchar(45) not null,
    password varchar(64) not null,
    constraint idusers_UNIQUE
        unique (idusers),
    constraint username_UNIQUE
        unique (username)
);

alter table users
    add primary key (idusers);

create table cryptofiles
(
    file_id binary(16) not null
        primary key,
    file_name varchar(45) null,
    time_added datetime not null,
    time_deletes datetime not null,
    file_type varchar(10) null,
    file_owner int null,
    constraint file_info_users_idusers_fk
        foreign key (file_owner) references users (idusers)
);

create table users_has_file_info
(
    users_idusers int not null,
    file_info_idfile_info binary(16) not null,
    primary key (users_idusers, file_info_idfile_info),
    constraint fk_users_has_file_info_file_info1
        foreign key (file_info_idfile_info) references cryptofiles (file_id),
    constraint fk_users_has_file_info_users1
        foreign key (users_idusers) references users (idusers)
);

create index fk_users_has_file_info_file_info1_idx
    on users_has_file_info (file_info_idfile_info);

create index fk_users_has_file_info_users1_idx
    on users_has_file_info (users_idusers);

