SELECT
	f.flight_no,
	CASE
    	WHEN tf.amount = MAX(tf.amount) OVER (PARTITION BY f.flight_no, tf.fare_conditions)
			AND COUNT(tf.amount) OVER (PARTITION BY f.flight_no, tf.fare_conditions) = 2
		THEN CONCAT(tf.fare_conditions, ' extra')
    	ELSE tf.fare_conditions
  	END AS fare_conditions,
	tf.amount
FROM ticket_flights tf
JOIN flights f ON f.flight_id = tf.flight_id
JOIN boarding_passes bp ON tf.ticket_no = bp.ticket_no
GROUP BY f.flight_no, tf.fare_conditions, tf.amount;