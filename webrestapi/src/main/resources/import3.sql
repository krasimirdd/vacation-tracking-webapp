DELIMITER $$
CREATE EVENT `vacation_days_update`
    ON SCHEDULE EVERY 1 YEAR STARTS '2019-01-01 16:00:00'
    DO
    UPDATE vacation.user as u
    SET u.days_available=20 and u.days_consumed = 0
$$
DELIMITER ;
